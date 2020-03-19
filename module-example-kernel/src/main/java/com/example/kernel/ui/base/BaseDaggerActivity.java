package com.example.kernel.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.kernel.net.exception.ExceptionHandler;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.yiciyuan.kernel.ui.activity.BaseMvpActivity;
import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.ui.base.BaseView;
import com.example.kernel.widget.LoadingProgressDialog;

import androidx.lifecycle.Lifecycle;
import androidx.viewbinding.ViewBinding;

/**
 * Created with Android Studio.
 * UserEntity: KCJ
 * Date: 2019-06-19 16:55
 * Description:
 */
public abstract class BaseDaggerActivity<P extends BasePresenter, V extends ViewBinding> extends BaseMvpActivity<P, V> implements BaseView, ExceptionHandler.Interceptor {

    private LoadingProgressDialog mProgressDialog;
    private final LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(this);

    @Override
    protected void created(Bundle savedInstanceState) {
        super.created(savedInstanceState);
        ExceptionHandler.getInstance().addIntercept(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public LifecycleProvider<Lifecycle.Event> getLifeCycleProvider() {
        return provider;
    }

    @Override
    public boolean intercept(Throwable throwable) {
        ExceptionHandler handler = ExceptionHandler.getInstance();
        int code = handler.transToCode(throwable);
//        if (code == ResultCode.ERROR_LOGIN_OUTTIME) {
////            showMsg("登陆过期，请重新登陆。");
//            if (AccountManager.getInstance().isLogin()) {
//                AccountManager.getInstance().logout();
//            }
//            return true;
//        }
        return false;
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

//    public Observable<Object> eventClick(View view) {
//        return eventClick(view, 1000);
//    }
//
//    public Observable<Object> eventClick(View view, int milliseconds) {
//        return RxView.clicks(view)
//                .throttleFirst(milliseconds, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    @Override
    protected void onDestroy() {
        ExceptionHandler.getInstance().removeIntercept(this);
        super.onDestroy();
    }
}
