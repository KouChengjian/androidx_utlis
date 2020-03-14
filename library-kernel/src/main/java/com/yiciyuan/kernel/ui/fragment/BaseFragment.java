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
import com.yiciyuan.kernel.widget.LoadingProgressDialog;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2019/1/7 13:47
 * Description:
 */
public abstract class BaseFragment extends Fragment implements IView, View.OnClickListener {

    protected Context mContext;
    protected Unbinder unbinder;
    protected LoadingProgressDialog mProgressDialog;

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
        if (getLayoutId() != 0 && getLayoutView() == null) {
            return inflater.inflate(getLayoutId(), container, false);
        } else if (getLayoutId() == 0 && getLayoutView() != null) {
            return getLayoutView();
        } else {
            throw new RuntimeException("getLayoutId() and getLayoutView() must be empty");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
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
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingProgressDialog(getContext());
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();
        }
        return mProgressDialog;
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
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
     * override this method to return content view id of the fragment
     */
    protected abstract int getLayoutId();

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
        if (null != unbinder) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }
}
