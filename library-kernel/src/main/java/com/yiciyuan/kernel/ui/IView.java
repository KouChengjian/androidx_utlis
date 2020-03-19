package com.yiciyuan.kernel.ui;

import android.app.ProgressDialog;
import android.content.Context;


/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2018/8/24 10:00
 * Description:
 */
public interface IView {

    Context getContext();

    ProgressDialog showProgressDialog(String msg);

    void dismissProgressDialog();

    void showMsg(String msg);

    void showMsg(int resId);
}
