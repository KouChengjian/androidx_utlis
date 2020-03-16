package com.example.kernel.net.transformer;


import com.example.kernel.net.exception.ApiException;
import com.example.kernel.net.exception.ResultCode;
import com.example.kernel.net.result.HttpResult;
import com.example.kernel.net.result.Taker;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2019-10-30 10:05
 * Description:
 */
public class ResultTransformer<T> implements SingleTransformer<HttpResult<T>, Taker<T>> {

    @Override
    public SingleSource<Taker<T>> apply(Single<HttpResult<T>> upstream) {
        return upstream
                .subscribeOn(Schedulers.io())
                .flatMap((Function<HttpResult<T>, SingleSource<Taker<T>>>) result -> {
                    int code = result.resCode;
                    if (code == ResultCode.SUCCESS) { // 请求成功，服务器返回了数据
                        return Single.just(Taker.ofNullable(result.data));
                    } else { // 请求失败，服务器返回约定的Code --> ApiException
                        return Single.error(new ApiException(result.resCode, result.resMsg));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}