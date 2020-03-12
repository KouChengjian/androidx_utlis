package com.yiciyuan.kernel.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.yiciyuan.kernel.ui.IView;
import com.yiciyuan.kernel.utils.AppManager;
import com.yiciyuan.kernel.utils.AppStartUtil;
import com.yiciyuan.kernel.utils.DoubleClickUtil;
import com.yiciyuan.kernel.utils.Toastor;
import com.yiciyuan.kernel.widget.LoadingProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2019/1/7 13:47
 * Description:
 */
public abstract class BaseActivity extends AppCompatActivity implements IView, View.OnClickListener {

    protected Unbinder unbinder;
    protected LoadingProgressDialog mProgressDialog;
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        create(savedInstanceState); // dagger2注解
        super.onCreate(savedInstanceState);
        AppManager.get().addActivity(this);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        if (getLayoutView() != null) {
            setContentView(getLayoutView());
        }
        unbinder = ButterKnife.bind(this);

        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId
                , new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();

        created(savedInstanceState);
        bindEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (DoubleClickUtil.isFastDoubleClick()) {
            return;
        }
        if (v.getId() == -1) {
            aminFinish();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public ProgressDialog showProgressDialog(String msg) {
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

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showMsg(String msg) {
        Toastor.showMsg(msg);
    }

    public void showMsg(int resId) {
        Toastor.showMsg(resId);
    }

    /**
     * Dagger2 use in your application module(not used in 'base' module)
     */
    protected abstract void create(Bundle savedInstanceState);

    /**
     * bind layout resource id
     */
    protected abstract int getLayoutId();

    /**
     * bind layout resource view
     */
    protected abstract View getLayoutView();

    /**
     * init views and events here
     */
    protected abstract void created(Bundle savedInstanceState);

    /**
     * init views and events here
     */
    protected void bindEvent() {
    }

    protected void startAnimActivity(Class<?> cls) {
        startAnimActivity(cls, null, -1, 0);
    }

    protected void startAnimActivity(Class<?> cls, int code) {
        startAnimActivity(cls, null, -1, code);
    }

    protected void startAnimActivity(Class<?> cls, Bundle bundle) {
        startAnimActivity(cls, bundle, 0);
    }

    protected void startAnimActivity(Class<?> cls, Bundle bundle, int code) {
        startAnimActivity(cls, bundle, -1, code);
    }

    protected void startAnimActivity(Class<?> cls, Bundle bundle, int flag, int code) {
        AppStartUtil.startAnimActivity(this, cls, bundle, flag, code);
    }

    public void finishResult(Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        finishResult(intent);
    }

    public void finishResult(Intent intent) {
        setResult(RESULT_OK, intent);
        this.finish();
    }

    public void aminFinish() {
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
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (null != unbinder) {
            unbinder.unbind();
        }
        AppManager.get().removeActivity(this);
        super.onDestroy();
    }
}
