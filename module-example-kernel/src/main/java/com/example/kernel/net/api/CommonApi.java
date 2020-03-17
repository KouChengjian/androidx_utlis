package com.example.kernel.net.api;


import com.example.kernel.BuildConfig;
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
public interface CommonApi {

    @GET("/manager/product/getListVenue")
    Single<HttpResult<HttpListResult<String>>> test2(@Query("username") String username);

}
