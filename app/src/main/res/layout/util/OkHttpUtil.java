package util;


import okhttp3.OkHttpClient;

/**
 * Email  : bigbigpeng3@gmail.com
 * Author : peng zhang
 */
public class OkHttpUtil {

    private static util.OkHttpUtil mInstance;

    private OkHttpClient mOkHttpClient;

    private OkHttpUtil() {
        mOkHttpClient = new OkHttpClient();
    }

    public static util.OkHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (util.OkHttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new util.OkHttpUtil();
                }
            }
        }
        return mInstance;
    }

    public OkHttpClient getOkHttpClient(){
        if (mOkHttpClient == null){
            return mOkHttpClient;
        }
        return new OkHttpClient();
    }

    /**
     * 封装的方法放在下面
     */



}