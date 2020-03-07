package com.yiciyuan.core.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.yiciyuan.core.ui.base.IPresenter;
import com.yiciyuan.core.ui.base.IView;
import com.yiciyuan.core.ui.base.PresenterHelper;


public class PresenterActivityImpl<T extends IView> extends PresenterBaseActivity implements IPresenter {

    protected T mView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        create(savedInstanceState);
        super.onCreate(savedInstanceState);
        try {
            mView = getViewClass().newInstance();
        } catch (Exception e) {
            mView = null;
            throw new RuntimeException(e.toString());
        }
        View view = mView.create(getLayoutInflater(), null);
        // TODO：目前vivo和oppo手机执行setContentView之后addIdleHandler不再回调
        setContentView(view);
        mView.bindPresenter(this);
        mView.created();
        mView.bindEvent();
        created(savedInstanceState);
    }

    //	@Override
    public Class<T> getViewClass() {
        return PresenterHelper.getViewClass(getClass());
    }

    @Override
    public void create(Bundle savedInstance) {
    }

    @Override
    public void created(Bundle savedInstance) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mView.destroy();
    }
}
