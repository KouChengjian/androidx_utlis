package com.yiciyuan.kernel.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2018/7/14 11:24
 * Description: 打开activity工具类
 */
public class AppStartUtil {

    public static void startAnimActivity(Activity activity, Class<?> cls) {
        startAnimActivity(activity, cls, null);
    }

    public static void startAnimActivity(Activity activity, Class<?> cls, Bundle bundle) {
        startAnimActivity(activity, cls, bundle, 0);
    }

    public static void startAnimActivity(Activity activity, Class<?> cls, Bundle bundle, int code) {
        startAnimActivity(activity, cls, bundle, -1, code);
    }

    public static void startAnimActivity(Activity activity, Class<?> cls, Bundle bundle, int flag, int code) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (flag != -1) {
            intent.setFlags(flag);
        }
        intent.setClass(activity, cls);
        if (code == 0) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, code);
        }
    }

    public static void startAnimActivity(Fragment fragment, Class<?> cls) {
        startAnimActivity(fragment, cls, null);
    }

    public static void startAnimActivity(Fragment fragment, Class<?> cls, Bundle bundle) {
        startAnimActivity(fragment, cls, bundle, 0);
    }

    public static void startAnimActivity(Fragment fragment, Class<?> cls, Bundle bundle, int code) {
        startAnimActivity(fragment, cls, bundle, -1, code);
    }

    public static void startAnimActivity(Fragment fragment, Class<?> cls, Bundle bundle, int flag, int code) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (flag != -1) {
            intent.setFlags(flag);
        }
        intent.setClass(fragment.getActivity(), cls);
        if (code == 0) {
            fragment.startActivity(intent);
        } else {
            fragment.startActivityForResult(intent, code);
        }
    }
}
