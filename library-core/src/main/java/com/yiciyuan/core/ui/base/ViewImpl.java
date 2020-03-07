package com.yiciyuan.core.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class ViewImpl implements IView {

	/**
     * create方法生成的view
     */
    protected View mRootView;
    
    /**
     * 绑定的presenter
     */
	protected IPresenter mPresenter;

	protected Unbinder unbinder;

	@Override
	public final View create(LayoutInflater inflater, ViewGroup container) {
		mRootView = inflater.inflate(getLayoutId(), container, false);
		unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
	}

	@Override
	public void created(){}
	
	@Override
	public void bindEvent() {}

	@Override
	public void createdDelay() {}

	@Override
	public void bindEventDelay() {}

	public Context getContext(){
		return mRootView.getContext();
	}

	@Override
	public <V extends View> V findViewById(int id) {
		return (V) mRootView.findViewById(id);
	}

	@Override
    public void bindPresenter(IPresenter presenter) {
        mPresenter = presenter;
    }

	@Override
	public void resume() {}

	@Override
	public void pause() {}

	@Override
	public void destroy() {
		if(unbinder != null){
			unbinder.unbind();
		}
	}
}
