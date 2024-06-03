package com.yidatong.debutwork;

import android.app.Application;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化异常捕获自定义类
        ExceptionCrashHandler ex= ExceptionCrashHandler.getInstance();
        ex.init(getApplicationContext());


    }
}
