package com.example;

import com.example.net.ApiHelper;
import com.example.net.result.Empty;
import com.example.net.result.HttpListResult;
import com.example.net.result.HttpResult;
import com.example.net.result.JsonParse;
import com.example.net.result.Taker;
import com.example.net.transformer.ResultJsonTransformer;
import com.example.net.transformer.ResultTransformer;

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
        RequestBody requestBody = JsonParse.createRequestBody(params);
        return ApiHelper.getInstance().userApi.login(requestBody)
                .compose(new ResultJsonTransformer<>())
                .map(Taker::get);
    }

    public Single<Empty> restPwd(String userPhone, String passWord , String verify) {
        return ApiHelper.getInstance().userApi.restPwd(userPhone , passWord , verify)
                .compose(new ResultTransformer<>())
                .map(Taker::get);
    }

    public Single<HttpListResult<List<String>>> getAuctionTypeList(String page) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        RequestBody requestBody = JsonParse.createRequestBody(params);
        return ApiHelper.getInstance().userApi.getAuctionTypeList(requestBody)
                .compose(new ResultTransformer<>())
                .map(Taker::get);
    }
}
