package com.example.kernel.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.kernel.widget.LoadingProgressDialog;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.yiciyuan.kernel.ui.activity.BaseMvpActivity;
import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.ui.base.BaseView;
import com.yiciyuan.kernel.ui.fragment.BaseMvpFragment;

import androidx.lifecycle.Lifecycle;
import androidx.viewbinding.ViewBinding;

/**
 * Created with Android Studio.
 * UserEntity: KCJ
 * Date: 2019-06-19 17:06
 * Description:
 */
public abstract class BaseDaggerFragment<P extends BasePresenter, V extends ViewBinding> extends BaseMvpFragment<P, V> implements BaseView {

    private LoadingProgressDialog mProgressDialog;
    private final LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(this);

    @Override
    protected void created(Bundle savedInstanceState) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public LifecycleProvider<Lifecycle.Event> getLifeCycleProvider() {
        return provider;
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
    public void onDestroy() {
        super.onDestroy();
    }
}

