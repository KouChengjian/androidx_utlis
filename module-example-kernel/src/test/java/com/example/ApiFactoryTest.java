package com.example;

import com.example.kernel.net.ApiHelper;
import com.example.kernel.net.result.Empty;
import com.example.kernel.net.result.HttpListResult;
import com.example.kernel.net.result.Taker;
import com.example.kernel.net.transformer.ResultJsonTransformer;
import com.example.kernel.net.transformer.ResultTransformer;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.RequestBody;

public class ApiFactoryTest {

    public Single<JSONObject> login(String userPhone, String passWord) {
        Map<String, Object> params = new HashMap<>();
        params.put("userPhone", userPhone);
        params.put("passWord", passWord);
        RequestBody requestBody = ApiHelper.getInstance().createRequestBody(params);
        return ApiHelper.getInstance().getUserApi().login(requestBody)
                .compose(new ResultJsonTransformer<>())
                .map(Taker::get);
    }

    public Single<Empty> restPwd(String userPhone, String passWord, String verify) {
        return ApiHelper.getInstance().getUserApi().restPwd(userPhone, passWord, verify)
                .compose(new ResultTransformer<>())
                .map(Taker::get);
    }

    public Single<HttpListResult<List<String>>> getAuctionTypeList(String page) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        RequestBody requestBody = ApiHelper.getInstance().createRequestBody(params);
        return ApiHelper.getInstance().getUserApi().getAuctionTypeList(requestBody)
                .compose(new ResultTransformer<>())
                .map(Taker::get);
    }
}
