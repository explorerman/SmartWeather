package com.lijie.smartweather.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by lijie on 17/5/22.
 */

public class BaseApplication extends Application {



    public static Context sAppContext;
    private static BaseApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();
        //初始化LitePalApplication，在这里直接传入this，不能在传入context
//        context.getApplicationContext();
        LitePalApplication.initialize(this);

        if (instance == null) {
            instance = this;
        }
    }
    public static Context getAppContext() {
        return sAppContext;
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
