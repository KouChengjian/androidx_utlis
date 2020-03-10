package com.yiciyuan.kernel.ui.activity;

import android.accounts.AccountManager;
import android.content.pm.ActivityInfo;
import android.database.Observable;
import android.os.Bundle;
import android.view.View;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.yiciyuan.kernel.net.exception.ExceptionHandler;
import com.yiciyuan.kernel.net.exception.ResultCode;
import com.yiciyuan.kernel.ui.BaseView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created with Android Studio.
 * UserEntity: KCJ
 * Date: 2019-06-19 16:55
 * Description:
 */
public abstract class BaseDaggerActivity extends BaseActivity implements BaseView, HasSupportFragmentInjector, ExceptionHandler.Interceptor {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;
    private final LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExceptionHandler.getInstance().addIntercept(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onDestroy() {
        ExceptionHandler.getInstance().removeIntercept(this);
        super.onDestroy();
    }

//    @Override
//    public boolean intercept(Throwable throwable) {
//        ExceptionHandler handler = ExceptionHandler.getInstance();
//        int code = handler.transToCode(throwable);
//        if (code == ResultCode.ERROR_LOGIN_OUTTIME) {
////            showMsg("登陆过期，请重新登陆。");
//            if (AccountManager.getInstance().isLogin()) {
//                AccountManager.getInstance().logout();
//            }
//            return true;
//        }
//        return false;
//    }

    @Override
    public LifecycleProvider<Lifecycle.Event> getLifeCycleProvider() {
        return provider;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

//    public Observable<Object> eventClick(View view) {
//        return eventClick(view, 1000);
//    }
//
//    public Observable<Object> eventClick(View view, int milliseconds) {
//        return RxView.clicks(view)
//                .throttleFirst(milliseconds, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread());
//    }
}
