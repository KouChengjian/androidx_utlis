package com.yiciyuan.kernel.ui;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2018/8/24 10:00
 * Description:
 */
public interface IPresenter<T> {

    void take(T view);

    void destroy(T view);
}
