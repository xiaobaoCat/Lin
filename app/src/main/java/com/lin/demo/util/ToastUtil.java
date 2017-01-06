package com.lin.demo.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by pc on 2016/12/15.
 * 防止一直弹出Toast
 */
public class ToastUtil {
    private static Toast toast = null;
    public static void showToast(Context context, String concent){
        if(toast!=null){
            toast.setText(concent);
            toast.setDuration(Toast.LENGTH_SHORT);
        }else{
            toast = Toast.makeText(context,concent, Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
