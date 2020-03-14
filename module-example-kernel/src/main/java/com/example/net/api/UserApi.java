package com.example.net.api;



import com.example.net.result.HttpListResult;
import com.example.net.result.HttpResult;
import com.yiciyuan.apt.annotation.ApiFactory;

import java.util.List;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiFactory
public interface UserApi {

    @Headers("Content-Type:application/json")
    @POST("/manager/user/login")
    Single<ResponseBody> login(@Body RequestBody body);

    @FormUrlEncoded
    @POST("api/restPwd")
    Single<HttpResult<String>> restPwd(@Field("username") String username, @Field("password") String password, @Field("verify") String verify);
//
    @Headers("Content-Type:application/json")
    @POST("/manager/product/getListVenue")
    Single<HttpResult<HttpListResult<List<String>>>> getAuctionTypeList(@Body RequestBody body);
}
