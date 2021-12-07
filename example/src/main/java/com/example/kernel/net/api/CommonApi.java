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
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

@ApiFactory(BuildConfig.APPLICATION_ID)
public interface CommonApi {

    @Headers("Content-Type:application/json")
    @POST("/manager/user/login")
    @ApiParams(paramName = {"username", "password"}, request = ApiRequestType.APPLICATIONJSON, response = ApiResponseType.JSONArray)
    Single<ResponseBody> login1(@Body RequestBody body);

    @Headers("Content-Type:application/json")
    @FormUrlEncoded
    @POST("/manager/user/login")
    @ApiParams(paramName = {"username2", "password1"}, request = ApiRequestType.FORMDATA, response = ApiResponseType.JSONObject)
    Single<ResponseBody> login2(@Field("username") String username, @Field("password") String password);


    @Headers("Content-Type:application/json")
    @POST("/manager/user/login")
    @ApiParams(paramName = {"username"}, request = ApiRequestType.APPLICATIONJSON)
    Single<HttpResult<String>> test1(@Body RequestBody body);

    @Headers("Content-Type:application/json")
    @POST("/manager/user/login")
    Single<HttpResult<String>> test2(@Field("username") String username);

    @GET("/manager/product/getListVenue")
    Single<HttpResult<HttpListResult<Empty>>> test3(@Query("username") String username);

    @GET("/manager/product/getListVenue")
    Single<HttpResult<HttpListResult<Empty>>> test4();
}
