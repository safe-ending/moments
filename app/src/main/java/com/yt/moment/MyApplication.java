package com.yt.moment;

import android.app.Application;


public class MyApplication extends Application {

    private static MyApplication myApplication;


    public static synchronized MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

    }

}
