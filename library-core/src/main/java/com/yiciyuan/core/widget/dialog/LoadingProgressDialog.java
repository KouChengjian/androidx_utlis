package com.yiciyuan.core.widget.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2020-03-07 14:40
 * Description:
 */
public class LoadingProgressDialog extends ProgressDialog {

    private Handler mDelayShow = new Handler();

    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            Activity activity = getActivity();
            if (activity == null || activity.isDestroyed()){
                return;
            }
            LoadingProgressDialog.super.show();
        }
    };

    public LoadingProgressDialog(Context context) {
        super(context);
    }

    public LoadingProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void show() {
        mDelayShow.removeCallbacks(mRun);
        Activity activity = getActivity();
        if (activity != null && getActivity().getWindow() != null){
            mDelayShow.postDelayed(mRun, 1000);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void showNoDelayed() {
        mDelayShow.removeCallbacks(mRun);
        super.show();
    }

    @Override
    public void dismiss() {
        mDelayShow.removeCallbacks(mRun);
        Activity activity = getActivity();
        if (activity != null && getActivity().getWindow() != null){
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        super.dismiss();
    }

    public Activity getActivity(){
        Context context = getContext();
        if (context instanceof ContextThemeWrapper){
            context = ((ContextThemeWrapper)getContext()).getBaseContext();
        }

        if (context instanceof Activity){
            return (Activity)context;
        }

        return null;
    }
}
