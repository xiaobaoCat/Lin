package util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.hengshuo.chengszj.activity.yunyingzhanghao.YuanWenLianJieInfoA;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pc on 2016/11/1.
 */

public class YuanWernLianJieUtil {

    public static void load(Context context, String url){
            Intent intent=	new Intent();
            intent.setClass(context, YuanWenLianJieInfoA.class);
            intent.putExtra("url",url);
            context.startActivity(intent);
    }

    public static void load(Context context, String url, String packageName){
        if(checkPackage(context,"com.taobao.taobao")){
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            context.startActivity(intent);
        }else{
            Intent intent=	new Intent();
            intent.setClass(context, YuanWenLianJieInfoA.class);
            intent.putExtra("url",url);
            context.startActivity(intent);
        }
    }

    /**
     * 检测该包名所对应的应用是否存在
     */
    public static boolean checkPackage(Context context, String packageName)	{
        if (packageName == null || "".equals(packageName)){
            return false;
        }
        try{
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }

    public static boolean checkURL(String url){
        boolean value=false;
        try {
            HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
            int code=conn.getResponseCode();
            System.out.println(">>>>>>>>>>>>>>>> "+code+" <<<<<<<<<<<<<<<<<<");
            if(code!=200){
                value=false;
            }else{
                value=true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

}
