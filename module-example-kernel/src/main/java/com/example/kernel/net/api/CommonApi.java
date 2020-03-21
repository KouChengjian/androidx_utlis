package com.example.kernel.net.api;


import com.example.kernel.BuildConfig;
import com.example.kernel.net.result.Empty;
import com.example.kernel.net.result.HttpListResult;
import com.example.kernel.net.result.HttpResult;
import com.yiciyuan.annotation.apt.ApiFactory;
import com.yiciyuan.annotation.apt.ApiParams;
import com.yiciyuan.annotation.enums.ApiRequestType;
import com.yiciyuan.annotation.enums.ApiResponseType;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

@ApiFactory(BuildConfig.APPLICATION_ID)
public interface CommonApi {

    @Headers("Content-Type:application/json")
    @POST("/manager/user/login")
    @ApiParams(paramName = {"username", "password"}, request = ApiRequestType.APPLICATIONJSON, response = ApiResponseType.JSONObject)
    Single<ResponseBody> login123(@Body RequestBody body);

    @GET("/manager/product/getListVenue")
    Single<HttpResult<String>> test2(@Query("username") String username);

    @GET("/manager/product/getListVenue")
    Single<HttpResult<HttpListResult<Empty>>> test3(@Query("username") String username);

}
