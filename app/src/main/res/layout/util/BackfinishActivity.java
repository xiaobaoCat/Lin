package util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baituo.me.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class BackfinishActivity extends Activity implements View.OnTouchListener {

    //手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 200;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 150;

    //记录手指按下时的横坐标。
    private float xDown;

    private float yDown;
    //记录手指移动时的横坐标。
    private float xMove;
    private float yMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;
    public ProgressDialog cpd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backfinish);

        LinearLayout ll = (LinearLayout) findViewById(R.id.activity_backfinish);
        ll.setOnTouchListener(this);

        FlymeSetStatusBarLightMode(getWindow(), true);
        MIUISetStatusBarLightMode(getWindow(), true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }




    }


    @SuppressLint("InlinedApi")
    public void setTranslucentStatus(boolean on) {
        Window win =getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.bg);//
    }

    //魅族
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }


    //小米
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }


    public <E extends View>E findView(Activity activity, int resId){

        return (E)activity.findViewById(resId);
    }

    public <E extends View>E findView(View view, int resId){

        return (E)view.findViewById(resId);
    }

    private static Toast mToast;
    public static void showToast(Activity context, String text) {
        if (mToast != null && !context.isFinishing()) {
            mToast.setText(text);
            mToast.show();
            return;
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.show();
    }



    public static void sendBroadcase(Context mContext, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        mContext.sendBroadcast(intent);
    }
    public static void sendBroadcase(Context mContext, String action, String data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("data", data);
        mContext.sendBroadcast(intent);
    }


    public static void registerReceiver(Context mContext, BroadcastReceiver mBroadcastReceiver, String action) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        mContext.registerReceiver(mBroadcastReceiver, filter);
    }
    public static void unregisterReceiver(Context mContext, BroadcastReceiver mBroadcastReceiver){
        mContext.unregisterReceiver(mBroadcastReceiver);
    }

    public void callPhoneDialog(Context context, final String phone) {
        AlterDialogTools.showQueRenDialog(context, "是否拨打以下电话", null, phone, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //拨打电话
                startActivity( new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phone)));
            }
        });
    }

    public void callPhone(Context context, final String phone) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call, null);
        final PopupWindow pwindow=new PopupWindow();
        pwindow.setContentView(view);//窗体的布局
        pwindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);//窗体布局宽
        pwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//窗体布局高
        pwindow.setFocusable(true);;//focuse设置为true，可点击，点击弹出窗体以外区域关闭弹出窗体。
        //设置一个半透明背景，规避点击弹出窗体以外区域不能关闭弹出窗体的问题。
        pwindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        pwindow.setOutsideTouchable(true);

        TextView tv_phone=(TextView) view.findViewById(R.id.tv_phone);
        tv_phone.setText(phone);
        view.findViewById(R.id.bt_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拨打电话
                startActivity( new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phone)));
            }
        });
        view.findViewById(R.id.bt_cannel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                pwindow.dismiss();
            }
        });

        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//弹出时的背景透明度设置
        getWindow().setAttributes(lp);
        pwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);//消失时设置回原来背景透明
            }
        });
        pwindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 判断某个服务是否正在运行的方法
     * @param mContext
     * @param serviceName  是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will// automatically handle clicks on the Home/Up button, so long// as you specify a parent activity in AndroidManifest.xml.int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


    // 转载请说明出处：http://blog.csdn.net/ff20081528/article/details/17845753
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove=event.getRawY();
                //活动的距离
                int distanceX = (int) (xMove - xDown);
                int distanceY=(int)(yMove - yDown);
                //获取顺时速度
                int xSpeed = getScrollVelocity();
                //当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
                if(distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN&&distanceY<=10&&distanceY>=-10) {
                    finish();
                    //设置切换动画，从右边进入，左边退出
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
    public void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    public void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    public int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }
}
