package com.yiciyuan.kernel.ui;

import com.trello.rxlifecycle2.LifecycleProvider;

import androidx.lifecycle.Lifecycle;


/**
 * Author  SLAN
 * <br>
 * 2018/9/5 20:18
 */
public interface BaseView extends IView {
    LifecycleProvider<Lifecycle.Event> getLifeCycleProvider();
}
