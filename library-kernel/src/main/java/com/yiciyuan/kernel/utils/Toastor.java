package com.yiciyuan.kernel.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yiciyuan.kernel.app.BaseApp;


public class Toastor {

    private static Toast mToast;
    private static Context context = null;

    public static Context getContext() {
        if (context == null) {
            context = BaseApp.app();
        }
        return context;
    }

    public static void showMsg(final String text) {
        if (getContext() == null) {
            return;
        }
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    public static void showMsg(final int resId) {
        if (getContext() == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(getContext().getApplicationContext(), resId,
                    Toast.LENGTH_LONG);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public Toast getSingletonToast(int resId) {
        if (getContext() == null) {
            return null;
        }
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        return mToast;
    }

    public Toast getSingletonToast(String text) {
        if (getContext() == null) {
            return null;
        }
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        return mToast;
    }

    public Toast getToast(int resId) {
        return Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT);
    }

    public Toast getToast(String text) {
        return Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
    }

    public Toast getLongToast(int resId) {
        return Toast.makeText(getContext(), resId, Toast.LENGTH_LONG);
    }

    public Toast getLongToast(String text) {
        return Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
    }


    public void showSingletonToast(int resId) {
        getSingletonToast(resId).show();
    }


    public void showSingletonToast(String text) {
        getSingletonToast(text).show();
    }

    public void showToast(int resId) {
        getToast(resId).show();
    }

    public void showToast(String text) {
        getToast(text).show();
    }

    public void showLongToast(int resId) {
        getLongToast(resId).show();
    }

    public void showLongToast(String text) {
        getLongToast(text).show();
    }


    public static void showToastView(int resId) {
        showToastView(getContext().getResources().getString(resId));
    }

    public static void showToastView(String s) {
//        View layoutView =  LayoutInflater.from(context).inflate(R.layout.common_toast_layout, null);
//        //设置文本的参数 设置加载文本文件的参数，必须通过LayoutView获取。
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        //layoutView.setLayoutParams(params);
//        TextView textView = (TextView)layoutView.findViewById(R.id.toast_content);
//        textView.setText(s);
//
//        //创建toast对象，
//        Toast toast = new Toast(context);
//        //把要Toast的布局文件放到toast的对象中
//        toast.setView(layoutView);
//        toast.setDuration(toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
    }
}
