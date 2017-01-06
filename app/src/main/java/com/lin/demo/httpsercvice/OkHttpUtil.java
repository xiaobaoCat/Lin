package com.lin.demo.httpsercvice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.lin.demo.AApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/12/19.
 */
public class OkHttpUtil {

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_MAX_AGE = 60*30; // 有网络时 设置缓存超时时间半个小时
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 ;
    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age="+CACHE_MAX_AGE;


    public static OkHttpClient getOkHttpClientNoCache(){
        return  new OkHttpClient.Builder()
                .connectTimeout(1000*10, TimeUnit.SECONDS)//连接超时
                .readTimeout(1000*10, TimeUnit.SECONDS)//读入超时
                .writeTimeout(1000*10, TimeUnit.SECONDS)//写入超时
                .addInterceptor(mLoggingInterceptor)//日志拦截器
                .build();
    }


    public static OkHttpClient getOkHttpClientCache(){
        //设置Cache缓存  记得添加读写权限
        //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        //<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
        File cacheFile = new File(AApplication.getInstance().getCacheDir(), "OkHttpCache");//缓存文件
        long maxSize=1024 * 1024 * 20;//缓存文件的最大尺寸20M
        Cache cache = new Cache(cacheFile, maxSize);//Cache缓存

        return  new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(1000*30, TimeUnit.SECONDS)//连接超时30s
                .readTimeout(1000*30, TimeUnit.SECONDS)//读入超时30s
                .writeTimeout(1000*30, TimeUnit.SECONDS)//写入超时30s
                .addInterceptor(mLoggingInterceptor)//日志拦截器
                //服务器不支持缓存,则响应头没有对应字段，则需要使用网络拦截器实现
                .addInterceptor(mCacheControlInterceptor)//应用拦截器,实现离线缓存
                .addNetworkInterceptor(mCacheControlInterceptor)//网络拦截器,实现在线缓存
                .build();
    }

    /**
     *  拦截器,打印请求的方式和URL和POST时的参数
     */
    private static final Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(request);
            long endTime = System.currentTimeMillis();
            long duration=endTime-startTime;

            Log.e("Method" ,request.method());//请求的方式 POST或GET等
            Log.e("Url" ,request.url().toString());//请求的url  GET请求参数拼接在url后面http://japi.juhe.cn/joke/content/list.from?page=1&pageSize=20  （page=1&pageSize=20为拼接的参数)
            Log.e("RequestTime" ,duration+"");//请求时间

            //便利POST请求的请求体(打印请求参数)
            if("POST".equals(request.method())){
                StringBuilder sb = new StringBuilder();
                if (request.body() instanceof FormBody) {
                    FormBody body = (FormBody) request.body();
                    for (int i = 0; i < body.size(); i++) {
                        sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                    }
                }
                Log.e("POSTBody",sb.toString());
            }

            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    };

    /**
     * 云端响应头拦截器，用来配置缓存策略
     */
    private static final Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            if (!isNetworkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
//            if(isNetworkAvailable()){
//                //有网的时候读接口上的 @Headers 里的配置，你可以在这里进行统一的设置
//                String cacheControl = request.cacheControl().toString();
//                return response.newBuilder()
//                        .header("Cache-Control",cacheControl)
//                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//                        .build();
//            }else{
                //没网的时候就读缓存
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        //缓存10分钟
                        .header("Cache-Control", "max-age=" + 60*10)
                        .build();
//            }
        }
    };


    public static SSLSocketFactory getSslSocketFactory(InputStream[] certificates, InputStream bksFile, String password){
        try{
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager trustManager = null;
            if (trustManagers != null){
                trustManager = (TrustManager) new MyTrustManager(chooseTrustManager(trustManagers));
            } else{
                trustManager = new UnSafeTrustManager();
            }
            sslContext.init(keyManagers, new TrustManager[]{trustManager}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e){
            throw new AssertionError(e);
        } catch (KeyManagementException e){
            throw new AssertionError(e);
        } catch (KeyStoreException e){
            throw new AssertionError(e);
        }
    }

    private class UnSafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session){
            return true;
        }
    }

    private static class UnSafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers(){
            return new X509Certificate[]{};
        }
    }

    private static TrustManager[] prepareTrustManager(InputStream... certificates){
        if (certificates == null || certificates.length <= 0) return null;
        try{
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates){
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try{
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e){
                }
            }
            TrustManagerFactory trustManagerFactory = null;
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            return trustManagers;
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (CertificateException e){
            e.printStackTrace();
        } catch (KeyStoreException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password){
        try{
            if (bksFile == null || password == null) return null;
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (UnrecoverableKeyException e){
            e.printStackTrace();
        } catch (CertificateException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers){
        for (TrustManager trustManager : trustManagers){
            if (trustManager instanceof X509TrustManager){
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private static class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try{
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce){
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers(){
            return new X509Certificate[0];
        }
    }

    public static SSLSocketFactory getSSlFactory(Context context) {

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(context.getAssets().open("client.cer"));//把证书打包在asset文件夹中
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext s = SSLContext.getInstance("TLSv1", "AndroidOpenSSL");
            s.init(null, tmf.getTrustManagers(), null);

            return s.getSocketFactory();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *  检查当前网络是否可用
     * @return 是否连接到网络
     */
    public static boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) AApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if(info != null && info.isConnected()){
                if(info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        Toast.makeText(AApplication.getInstance(), "网络异常，请检查网络设置", Toast.LENGTH_SHORT).show();
        return false;
    }

    //http://japi.juhe.cn/joke/content/list.from?key=ec58a6b65513980369a97cbed0c8ed17&page=1&pageSize=20&time=1483511665&sort=desc
    //http://japi.juhe.cn/joke/content/list.from?sort=desc&page=1&pagesize=20&time=1483511665&key=ec58a6b65513980369a97cbed0c8ed17
}
