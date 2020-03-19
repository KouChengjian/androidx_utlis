package com.yiciyuan.kernel.ui.base;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.yiciyuan.kernel.ui.IView;

import androidx.lifecycle.Lifecycle;


/**
 * Created with Android Studio.
 */
public interface BaseView extends IView {
    LifecycleProvider<Lifecycle.Event> getLifeCycleProvider();
}
