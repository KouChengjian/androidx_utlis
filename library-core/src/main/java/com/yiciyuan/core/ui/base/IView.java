package com.yiciyuan.core.ui.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * View层的根接口 
 */
public interface IView {
    /**
     * 根据 getLayoutId 方法生成生成setContentView需要的根布局
     */
    View create(LayoutInflater inflater, ViewGroup container);

    /**
     * 当Activity的onCreate完毕后调用
     */
    void created();

    /**
     * 当Activity myQueue 空闲的时候在开始执行
     * 延时是都使用延时周期函数
     */
    void createdDelay();

    /**
     * 返回当前视图需要的layout的id
     */
    int getLayoutId();

    /**
     * 根据id获取view
     */
    <V extends View> V findViewById(int id);

    /**
     * 绑定Presenter
     */
    void bindPresenter(IPresenter presenter);

    /**
     * created 后调用，可以调用click
     * 等方法为控件设置点击事件，一般推荐使用click(IPresenter presenter, View ...views
     * 方法并且让你的Presenter实现相应接口。
     */
    void bindEvent();

    /**
     * 延时是都使用延时周期函数
     */
    void bindEventDelay();

    void resume();

    void pause();

    void destroy();
}
