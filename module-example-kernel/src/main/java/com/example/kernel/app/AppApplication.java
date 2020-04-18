package com.example.kernel.app;

import android.app.Application;

import com.yiciyuan.kernel.BuildConfig;
import com.yiciyuan.kernel.app.BaseApp;
import com.yiciyuan.utils.glide.GlideUtil;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2020-03-03 16:14
 * Description:
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApp.Ext.init(this);
        BaseApp.Ext.setDebug(BuildConfig.DEBUG);
        GlideUtil.Ext.init(this);
        GlideUtil.Ext.setReferer("https://t-app.shangyizhijia.com"); // 防盗链
    }
}
