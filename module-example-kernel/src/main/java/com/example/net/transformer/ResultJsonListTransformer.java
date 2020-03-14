package com.example.net.transformer;

import com.example.net.exception.ApiException;
import com.example.net.exception.ResultCode;
import com.example.net.result.Taker;
import com.example.net.result.JsonParse;

import org.json.JSONArray;
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
 * User: kcj
 * Date: 2019-12-20 16:59
 * Description:
 */
public class ResultJsonListTransformer <T> implements SingleTransformer<ResponseBody, Taker<JSONArray>> {

    @Override
    public SingleSource<Taker<JSONArray>> apply(Single<ResponseBody> upstream) {
        return upstream
                .subscribeOn(Schedulers.io())
                .flatMap((Function<ResponseBody, SingleSource<Taker<JSONArray>>>) result -> {
                    JSONObject jsonObject = JsonParse.createJSONObject(result.string());
                    int code = JsonParse.getInt(jsonObject ,"resCode");
                    String msg = JsonParse.getString(jsonObject ,"resMsg");
                    if (code == ResultCode.SUCCESS) { // 请求成功，服务器返回了数据
                        JSONArray data = JsonParse.getJSONArray(jsonObject , "data");
                        return Single.just(Taker.ofNullable(data));
                    } else { // 请求失败，服务器返回约定的Code --> ApiException
                        return Single.error(new ApiException(code, msg));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
