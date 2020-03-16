package com.example.kernel.net.result;


import com.example.kernel.net.exception.ExceptionHandler;
import com.yiciyuan.kernel.utils.LogUtil;
import com.yiciyuan.kernel.utils.Toastor;

import java.util.concurrent.CancellationException;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Create by AItsuki on 2018/7/19.
 */
public abstract class ResultObserver<T> implements SingleObserver<T> {

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onError(Throwable e) {
        // RxLifecycle取消Single或Completed的订阅时会主动抛此异常
        if (e instanceof CancellationException) {
            return;
        }

        ExceptionHandler handler = ExceptionHandler.getInstance();
        int code = handler.transToCode(e);
        String msg = handler.transToReason(e);
        if (!handler.intercept(e)) {

        }
        onError(e, code, msg);
    }

    /**
     * 如果异常被拦截器拦截，将不会回调这个方法，如果一定要，可以重载onError(Throwable e)方法
     *
     * @param e   抓获的异常
     * @param msg 推荐Toast使用的msg
     */
    public void onError(Throwable e, int code, String msg) {
        Toastor.showMsg(msg);
        LogUtil.e(msg);
    }
}
