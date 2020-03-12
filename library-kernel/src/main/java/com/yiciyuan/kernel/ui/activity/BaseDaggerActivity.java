package com.yiciyuan.kernel.ui.activity;

import android.os.Bundle;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.yiciyuan.kernel.net.exception.ExceptionHandler;
import com.yiciyuan.kernel.ui.base.BaseView;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created with Android Studio.
 * UserEntity: KCJ
 * Date: 2019-06-19 16:55
 * Description:
 */
public abstract class BaseDaggerActivity extends BaseActivity implements BaseView, HasSupportFragmentInjector, ExceptionHandler.Interceptor {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;
    private final LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExceptionHandler.getInstance().addIntercept(this);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public LifecycleProvider<Lifecycle.Event> getLifeCycleProvider() {
        return provider;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

    @Override
    protected void onDestroy() {
        ExceptionHandler.getInstance().removeIntercept(this);
        super.onDestroy();
    }
}
