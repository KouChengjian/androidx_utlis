package com.yiciyuan.kernel.ui.fragment;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.yiciyuan.kernel.ui.base.BaseView;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created with Android Studio.
 * UserEntity: KCJ
 * Date: 2019-06-19 17:06
 * Description:
 */
public abstract class BaseDaggerFragment extends BaseFragment implements BaseView, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;
    private final LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(this);

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }

    @Override
    public LifecycleProvider<Lifecycle.Event> getLifeCycleProvider() {
        return provider;
    }
}

