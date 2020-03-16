package com.example.kernel.conf;

import android.content.Context;

import com.example.kernel.cache.AccountManager;
import com.example.kernel.widget.dialog.debug.ServerUrl;
import com.yiciyuan.kernel.app.BaseApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2018/8/3 16:09
 * Description: url配置
 */
public class AppConfig {

    public static ServerUrl SERVER = null;
    public final static List<ServerUrl> DefaultServerUrlList = new ArrayList<>();
    public final static ServerUrl URL_SERVER_PROP_TEST = new ServerUrl(
            "https://t-market.shangyizhijia.com/app/marketing/"
    );

    static {
        DefaultServerUrlList.add(URL_SERVER_PROP_TEST); // tt
    }

    public static void init(Context context) {
        new AppConfig(context);
    }

    public AppConfig(Context context) {
        if (BaseApp.isDebug()) {
            int postion = AccountManager.getInstance().getServerUrl(context);
            if (DefaultServerUrlList.size() <= postion) {
                postion = 0;
            }
            SERVER = DefaultServerUrlList.get(postion);
        } else {
            SERVER = URL_SERVER_PROP_TEST;
        }
    }
}
