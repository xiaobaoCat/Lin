package com.lin.demo;

import android.app.Activity;
import android.app.Application;
import java.util.ArrayList;
import java.util.List;

public class AApplication extends Application {
    private static AApplication instance;
    public static AApplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }


    //退出程序
    private List<Activity> activities = new ArrayList<>();
    public void addActivity(Activity activity) {
        activities.add(activity);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : activities) {
            activity.finish();
        }
        System.exit(0);
    }
    public void changeAccount() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }
}
