package com.example.kernel.net.api;


import com.example.kernel.BuildConfig;
import com.example.kernel.entity.po.UserEntity;
import com.example.kernel.net.result.Empty;
import com.example.kernel.net.result.HttpListResult;
import com.example.kernel.net.result.HttpResult;
import com.yiciyuan.annotation.apt.ApiFactory;
import com.yiciyuan.annotation.apt.ApiParams;
import com.yiciyuan.annotation.enums.ApiResultType;

import java.util.List;

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
public interface UserApi {

    @Headers("Content-Type:application/json")
    @POST("/manager/user/login")
    @ApiParams(paramName = {"username", "password"})
    Single<ResponseBody> login(@Body RequestBody body);

    @Headers("Content-Type:application/json")
    @POST("/manager/user/login")
    @ApiParams(paramName = {"username", "password"},result = ApiResultType.Array)
    Single<ResponseBody> loginWeixin(@Body RequestBody body);

    @FormUrlEncoded
    @POST("api/restPwd")
    Single<HttpResult<Empty>> restPwd(@Field("username") String username, @Field("password") String password, @Field("verify") String verify);

    @GET("/manager/product/getListVenue")
    Single<HttpResult<HttpListResult<String>>> test1(@Query("username") String username);

    @Headers("Content-Type:application/json")
    @POST("/manager/product/getListVenue")
    Single<HttpResult<HttpListResult<List<String>>>> getAuctionTypeList(@Body RequestBody body);
}
