package com.example.kernel.app;

import android.app.Application;

import com.example.BuildConfig;
import com.yiciyuan.core.app.BaseApp;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2020-03-07 17:36
 * Description:
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApp.Ext.init(this);
        BaseApp.Ext.setDebug(BuildConfig.DEBUG);
    }
}
