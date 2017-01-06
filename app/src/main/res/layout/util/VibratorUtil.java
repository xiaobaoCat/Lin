package util;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/** 
 * 手机震动工具类 
 * 1.声明权限
 * <uses-permission android:name="android.permission.VIBRATE"/>
 * 2. 获得震动服务。
 *  Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
 *   3.启动震动
 *   ib.vibrate(milliseconds);      
 */  
public class VibratorUtil {  
    /** 
     * final Activity activity  ：调用该方法的Activity实例 。
     * long milliseconds ：震动的时长，单位是毫秒，只震动一次 。
     */  
     public static void Vibrate(final Context context, long milliseconds) {
    	// 获得震动服务。
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        //启动震动
        vib.vibrate(milliseconds);   
     }  
     
     /** 
      * final Activity activity  ：调用该方法的Activity实例 
      * long[] pattern  ：指代一个震动的频率数组。每两个为一组，每组的第一个为等待时间，第二个为震动时间。
      * 比如  [2000,500,100,400],会先等待2000毫秒，震动500，再等待100，震动400
      * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次 
      */ 
     public static void Vibrate(final Context context, long[] pattern, boolean isRepeat) {
            Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(pattern, isRepeat ? 1 : -1);   
     }   
}  
