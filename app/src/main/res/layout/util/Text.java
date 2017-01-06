package util;

import android.app.Notification;
import android.content.Context;
import android.media.AudioManager;

/**
 * Created by Administrator on 2016/12/1.
 */

public class Text {

    //首先需要接收一个Notification的参数
    //1 声音+震动    2声音+无震动    3无声+震动  4无声+无震动
    public static void setAlarmParams(Context mContext, Notification notification, int type) {
        AudioManager volMgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        switch (volMgr.getRingerMode()) {//获取系统设置的铃声模式
            case AudioManager.RINGER_MODE_SILENT://静音模式，值为0，这时候不震动，不响铃
                notification.sound = null;
                notification.vibrate = null;
                break;
            case AudioManager.RINGER_MODE_VIBRATE://震动模式，值为1，这时候震动，不响铃
                notification.sound = null;
                if(type==1||type==3){//有震动
                    notification.defaults |= Notification.DEFAULT_VIBRATE;
                }
                if(type==2||type==4){//无震动
                    notification.vibrate = null;
                }
                break;
            case AudioManager.RINGER_MODE_NORMAL://常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动

                if(volMgr.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) ==1){//1震动
                    if(type==1){//声音+震动
                        notification.defaults |= Notification.DEFAULT_SOUND;
                        notification.defaults |= Notification.DEFAULT_VIBRATE;
                    }
                    if(type==2){//声音+无震动
                        notification.defaults |= Notification.DEFAULT_SOUND;
                        notification.vibrate = null;
                    }
                    if(type==3){//无声+震动
                        notification.sound = null;
                        notification.defaults |= Notification.DEFAULT_VIBRATE;
                    }
                    if(type==4){//无声音+无震动
                        notification.sound = null;
                        notification.vibrate = null;
                    }
                }

                if(volMgr.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == 0){//0不震动
                    if(type==1){//声音+震动
                        notification.defaults |= Notification.DEFAULT_SOUND;
                        notification.vibrate = null;
                    }
                    if(type==2){//声音+无震动
                        notification.defaults |= Notification.DEFAULT_SOUND;
                        notification.vibrate = null;
                    }
                    if(type==3){//无声+震动
                        notification.sound = null;
                        notification.vibrate = null;
                    }
                    if(type==4){//无声音+无震动
                        notification.sound = null;
                        notification.vibrate = null;
                    }
                }
                if(volMgr.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == 2) {//只2在静音时震动
                    if (type == 1) {//声音+震动
                        notification.defaults |= Notification.DEFAULT_VIBRATE;
                        notification.vibrate = null;
                    }
                    if (type == 2) {//声音+无震动
                        notification.defaults |= Notification.DEFAULT_VIBRATE;
                        notification.vibrate = null;
                    }
                    if (type == 3) {//无声+震动
                        notification.sound = null;
                        notification.defaults |= Notification.DEFAULT_SOUND;
                    }
                    if (type == 4) {//无声音+无震动
                        notification.sound = null;
                        notification.vibrate = null;
                    }
                    notification.flags |= Notification.FLAG_SHOW_LIGHTS;//都给开灯
                    break;
                }
        }
    }

}

//
//        //0位静音，1为震动，2为响铃，下面就是通知的设置
//        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        if((boolean) SPFUtils.get(context, "soundReminder", true)){
//            am.setRingerMode(2);
//        }else{
//            am.setRingerMode(0);
//        }
//        if ((boolean) SPFUtils.get(context, "vibrationReminder", true)) {
//            Log.e("有振动", (boolean) SPFUtils.get(context, "vibrationReminder", true)+"");
//        }else {
//            Log.e("无振动", (boolean) SPFUtils.get(context, "vibrationReminder", true)+"");
//            VibratorUtil.Vibrate(context,2000);
//        }

