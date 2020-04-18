package com.example.kernel.cache;

import com.yiciyuan.kernel.utils.LogUtil;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class RoomObserver<T> implements SingleObserver<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(e.toString());
    }
}
