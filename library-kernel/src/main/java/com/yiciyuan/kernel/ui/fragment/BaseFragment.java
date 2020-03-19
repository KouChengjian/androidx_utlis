package com.yiciyuan.kernel.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiciyuan.kernel.ui.IView;
import com.yiciyuan.kernel.utils.AppStartUtil;
import com.yiciyuan.kernel.utils.DoubleClickUtil;
import com.yiciyuan.kernel.utils.Toastor;

import androidx.fragment.app.Fragment;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2019/1/7 13:47
 * Description:
 */
public abstract class BaseFragment extends Fragment implements IView, View.OnClickListener {

    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        create(savedInstanceState);
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutView();
        if (view != null) {
            return view;
        } else {
            throw new RuntimeException("getLayoutView() must be empty");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        created(savedInstanceState);
        bindEvent();
    }

    @Override
    public void onClick(View v) {
        if (DoubleClickUtil.isFastDoubleClick()) {
            return;
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public ProgressDialog showProgressDialog(String msg) {
        return null;
    }

    @Override
    public void dismissProgressDialog() {
    }

    @Override
    public void showMsg(String msg) {
        Toastor.showMsg(msg);
    }

    public void showMsg(int resId) {
        Toastor.showMsg(resId);
    }

    /**
     * Dagger2 use in your application module(not used in 'base' module)
     */
    protected abstract void create(Bundle savedInstanceState);

    /**
     * bind layout resource view
     */
    protected abstract View getLayoutView();

    /**
     * override this method to do operation in the fragment
     */
    protected abstract void created(Bundle savedInstanceState);

    /**
     * override this method to do operation in the fragment
     */
    protected void bindEvent() {
    }

    protected LayoutInflater getInflater(){
        return LayoutInflater.from(getContext());
    }

    protected void startAnimActivity(Class<?> cls) {
        startAnimActivity(cls, null, 0);
    }

    protected void startAnimActivity(Class<?> cls, int code) {
        startAnimActivity(cls, null, code);
    }

    protected void startAnimActivity(Class<?> cls, Bundle bundle) {
        startAnimActivity(cls, bundle, 0);
    }

    protected void startAnimActivity(Class<?> cls, Bundle bundle, int code) {
        AppStartUtil.startAnimActivity(this, cls, bundle, -1, code);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
