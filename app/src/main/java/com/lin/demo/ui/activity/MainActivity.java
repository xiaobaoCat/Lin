package com.lin.demo.ui.activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.lin.demo.AApplication;
import com.lin.demo.R;
import com.lin.demo.interfaces.OnTabReselectListener;
import com.lin.demo.ui.fragment.FragmentTab1;
import com.lin.demo.ui.fragment.FragmentTab2;
import com.lin.demo.ui.fragment.FragmentTab3;
import com.lin.demo.ui.fragment.FragmentTab4;
import com.lin.demo.util.ToastUtil;
import com.lin.demo.widget.MyFragmentTabHost;

public class MainActivity extends BaseActivity implements  View.OnTouchListener{

    private MainActivity mContext;
    private MyFragmentTabHost mTabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        initTabHost();
    }

    private void initTabHost() {
        mTabHost = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //Tab为View类型
        View tab1 = getLayoutInflater().inflate(R.layout.tab1, null);
        View tab2 = getLayoutInflater().inflate(R.layout.tab2, null);
        View tab3 = getLayoutInflater().inflate(R.layout.tab3, null);
        View tab4 = getLayoutInflater().inflate(R.layout.tab4, null);
        mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator(tab1), FragmentTab1.class,null);
        mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator(tab2), FragmentTab2.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Tab3").setIndicator(tab3), FragmentTab3.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Tab4").setIndicator(tab4), FragmentTab4.class, null);

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouchEvent(event);
        boolean consumed = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN && v.equals(mTabHost.getCurrentTabView())) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(mTabHost.getCurrentTabTag());
            if (currentFragment != null&& currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
                consumed = true;
            }
        }
        return consumed;
    }

    private long exitTime = 0;
    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast(mContext,"再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            AApplication.getInstance().onTerminate();//遍历退出所有打开的Activity
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
