package com.example.kernel.ui.base;

import android.os.Bundle;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.yiciyuan.kernel.ui.activity.BaseMvpActivity;
import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.ui.base.BaseView;

import androidx.lifecycle.Lifecycle;
import androidx.viewbinding.ViewBinding;

/**
 * Created with Android Studio.
 * UserEntity: KCJ
 * Date: 2019-06-19 17:06
 * Description:
 */
public abstract class BaseDaggerFragment<P extends BasePresenter, V extends ViewBinding> extends BaseMvpActivity<P, V> implements BaseView {

    private final LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(this);

    @Override
    protected void created(Bundle savedInstanceState) {

    }

    @Override
    public LifecycleProvider<Lifecycle.Event> getLifeCycleProvider() {
        return provider;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

