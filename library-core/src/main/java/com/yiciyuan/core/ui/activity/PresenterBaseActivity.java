package com.yiciyuan.core.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.yiciyuan.core.utils.AppManager;
import com.yiciyuan.core.utils.AppStartUtil;
import com.yiciyuan.core.utils.Toastor;
import com.yiciyuan.core.widget.dialog.LoadingProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2020-03-07 14:24
 * Description:
 */
public class PresenterBaseActivity extends AppCompatActivity implements View.OnClickListener {

    private LoadingProgressDialog mProgressDialog;
    private int activityCloseEnterAnimation;
    private int activityCloseExitAnimation;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.get().addActivity(this);

        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == -1) {
            aminFinish();
        }
    }

    protected Context getContext() {
        return this;
    }

    public void showMsg(String msg) {
        Toastor.showMsg(msg);
    }

    public void showMsg(int resId) {
        Toastor.showMsg(resId);
    }

    public LoadingProgressDialog startLoading(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingProgressDialog(getContext());
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.showNoDelayed();
        } else {
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();
        }
        return mProgressDialog;
    }

    public LoadingProgressDialog startLoadingDelayed(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingProgressDialog(getContext());
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();
        }
        return mProgressDialog;
    }

    protected void startAnimActivity(Class<?> cls) {
        startAnimActivity(cls, null, 0);
    }

    protected void startAnimActivity(Class<?> cls, int code) {
        startAnimActivity(cls, null, code);
    }

    protected void startAnimActivity(Class<?> cls, Bundle bundle) {
        startAnimActivity(cls, bundle, 0);
    }

    protected void startAnimActivity(Class<?> cls, Bundle bundle, int code) {
        AppStartUtil.startAnimActivity(this, cls, bundle, -1, code);
    }

    public void finishResult(Intent intent) {
        setResult(RESULT_OK, intent);
        this.finish();
    }

    protected void aminFinish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            aminFinish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AppManager.get().removeActivity(this);
        super.onDestroy();
    }
}
