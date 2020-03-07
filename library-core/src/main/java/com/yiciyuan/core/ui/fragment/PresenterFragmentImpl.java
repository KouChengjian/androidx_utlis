package com.yiciyuan.core.ui.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiciyuan.core.ui.base.IPresenter;
import com.yiciyuan.core.ui.base.IView;
import com.yiciyuan.core.ui.base.PresenterHelper;
import com.yiciyuan.core.utils.MarkerUtil;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2020-03-07 15:13
 * Description:
 */
public class PresenterFragmentImpl<T extends IView> extends PresenterBaseFragment implements IPresenter {

    protected T mView;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        create(savedInstanceState);
        try {
            mView = getViewClass().newInstance();
        } catch (Exception e) {
            mView = null;
            throw new RuntimeException(e.toString());
        }

        View view = mView.create(inflater, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView.bindPresenter(this);
        mView.created();
        mView.bindEvent();
        created(savedInstanceState);
    }

    //    @Override
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
    public void onResume() {
        super.onResume();
        mView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView.destroy();
    }
}
