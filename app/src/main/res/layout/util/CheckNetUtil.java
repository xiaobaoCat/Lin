package util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by Administrator on 2016/11/28.
 */

public class CheckNetUtil {

    public static void showSetNetworkDialog(final Context context) {
        AlertDialog mshowDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("设置网络");
        builder.setMessage("网络错误请检查网络链接");
        builder.setPositiveButton("设置网络", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        mshowDialog= builder.create();
        mshowDialog.setCanceledOnTouchOutside(false);
        mshowDialog.show();

    }


    /**
     * 判断网络是否连通
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

//判断gps是否开启
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    /**
     * 判断定位是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 打开Gps设置界面
     */
    public static void openGpsSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void showSetGpsDialog(final Context context) {
        AlertDialog mshowDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("打开GPS");
        builder.setMessage("GPS未开启，是否现在设置?");
        builder.setPositiveButton("设置GPS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                openGpsSettings(context);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        mshowDialog= builder.create();
        mshowDialog.setCanceledOnTouchOutside(false);
        mshowDialog.show();

    }


}
