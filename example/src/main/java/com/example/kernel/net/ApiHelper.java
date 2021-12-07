package com.example.kernel.net;

import com.example.kernel.conf.AppConfig;
import com.example.kernel.net.api.CommonApi;
import com.example.kernel.net.api.UserApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yiciyuan.annotation.apt.ApiConfig;
import com.yiciyuan.kernel.app.BaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {

    private final int CONNECT_TIMEOUT = 10;
    private final int READ_TIMEOUT = 10;
    private final int WRITE_TIMEOUT = 10;

    private Retrofit retrofit;
    private UserApi userApi;
    private CommonApi commonApi;

    private static class SingletonHolder {
        private static final ApiHelper INSTANCE = new ApiHelper();
    }

    public static ApiHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ApiHelper() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        File cacheFile = new File(BaseApp.app().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
//                .addInterceptor(headInterceptor)
//                .addInterceptor(logInterceptor)
//                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache)
                .build();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(AppConfig.SERVER.baseURL)
                .build();

        userApi = createApi(UserApi.class);
        commonApi = createApi(CommonApi.class);
    }

    private <T> T createApi(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    private JSONObject mapToJson(Map map) {
        JSONObject jsonObject = new JSONObject();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            try {
                jsonObject.put(key, entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    public UserApi getUserApi() {
        return userApi;
    }

    public CommonApi getCommonApi() {
        return commonApi;
    }

    public RequestBody createRequestBody(Map map) {
        JSONObject jsonObject = mapToJson(map);
        return createRequestBody(-1, -1, jsonObject);
    }

    public RequestBody createRequestBody(int pageNum, int pageSize, Map map) {
        JSONObject jsonObject = mapToJson(map);
        return createRequestBody(pageNum, pageSize, jsonObject);
    }

    public RequestBody createRequestBody(JSONObject json) {
        return createRequestBody(-1, -1, json);
    }

    public RequestBody createRequestBody(int pageNum, int pageSize, JSONObject json) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (pageNum != -1 && pageSize != -1) {
                jsonObject.put("pageNum", pageNum);
                jsonObject.put("pageSize", pageSize);
            }
            jsonObject.put("recData", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type:application/json"), jsonObject.toString());
        return requestBody;
    }

    public RequestBody createRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public RequestBody createImageRequestBody(File file) {
        return RequestBody.create(MediaType.parse("image/png"), file);
    }
}
