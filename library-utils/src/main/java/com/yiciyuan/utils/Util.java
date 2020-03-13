package com.yiciyuan.utils;

import android.content.Context;

/**
 * Created with Android Studio.
 * 需要用到一些Context
 */
public enum Util {
    INSTANCE;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
