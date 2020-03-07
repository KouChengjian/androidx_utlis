package com.yiciyuan.core.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yiciyuan.core.utils.AppStartUtil;
import com.yiciyuan.core.utils.Toastor;
import com.yiciyuan.core.widget.dialog.LoadingProgressDialog;

import androidx.fragment.app.Fragment;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2020-03-07 15:05
 * Description:
 */
public class PresenterBaseFragment extends Fragment {

    private LoadingProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public View findViewById(int paramInt) {
        return getView().findViewById(paramInt);
    }

    protected void showMsg(final String text) {
        Activity activity = getActivity();
        if (activity == null || TextUtils.isEmpty(text)) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toastor.showMsg(text);
            }
        });
    }

    protected void showMsg(final int resId) {
        Activity activity = getActivity();
        if (activity == null || resId == 0) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toastor.showMsg(resId);
            }
        });
    }

    public LoadingProgressDialog startLoading(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingProgressDialog(getContext());
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.showNoDelayed();
        } else {
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();
        }
        return mProgressDialog;
    }

    public LoadingProgressDialog startLoadingDelayed(String msg) {
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
