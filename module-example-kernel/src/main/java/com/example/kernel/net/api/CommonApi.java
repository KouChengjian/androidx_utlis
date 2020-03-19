package com.example.kernel.net.api;


import com.example.kernel.BuildConfig;
import com.example.kernel.net.result.HttpListResult;
import com.example.kernel.net.result.HttpResult;
import com.yiciyuan.annotation.apt.ApiFactory;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

@ApiFactory(BuildConfig.APPLICATION_ID)
public interface CommonApi {

    @GET("/manager/product/getListVenue")
    Single<HttpResult<HttpListResult<String>>> test2(@Query("username") String username);

}
