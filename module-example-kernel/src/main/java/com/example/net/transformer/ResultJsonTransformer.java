package com.example.net.transformer;


import com.example.net.exception.ApiException;
import com.example.net.exception.ResultCode;
import com.example.net.result.JsonParse;
import com.example.net.result.Taker;

import org.json.JSONObject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created with Android Studio.
 * UserEntity: kcj
 * Date: 2019/1/10 17:30
 * Description:
 */
public class ResultJsonTransformer<T> implements SingleTransformer<ResponseBody, Taker<JSONObject>> {

    @Override
    public SingleSource<Taker<JSONObject>> apply(Single<ResponseBody> upstream) {
        return upstream
                .subscribeOn(Schedulers.io())
                .flatMap((Function<ResponseBody, SingleSource<Taker<JSONObject>>>) result -> {
                    JSONObject jsonObject = JsonParse.createJSONObject(result.string());
                    int code = JsonParse.getInt(jsonObject ,"resCode");
                    String msg = JsonParse.getString(jsonObject ,"resMsg");
                    if (code == ResultCode.SUCCESS) { // 请求成功，服务器返回了数据
                        JSONObject data = JsonParse.getJSONObject(jsonObject , "data");
                        return Single.just(Taker.ofNullable(data));
                    } else { // 请求失败，服务器返回约定的Code --> ApiException
                        return Single.error(new ApiException(code, msg));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
