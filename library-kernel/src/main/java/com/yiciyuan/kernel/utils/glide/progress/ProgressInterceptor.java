package com.yiciyuan.kernel.utils.glide.progress;

import android.text.TextUtils;

import com.bumptech.glide.load.HttpException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2019/4/1 20:07
 * Description:
 */
public class ProgressInterceptor implements Interceptor {

    public static final String PROGRESS_INTERCEPTOR_KEY = "PROGRESS";
    private static final ConcurrentHashMap<String, WeakReference<ProgressListener>> LISTENERS = new ConcurrentHashMap<>();


    //入注册下载监听
    public static void addListener(String url, ProgressListener progressListener) {
        url = getUrlNoParams(url);
        LISTENERS.put(url, new WeakReference<>(progressListener));
    }

    //取消注册下载监听
    public static void removeListener(String url) {
        url = getUrlNoParams(url);
        if (!TextUtils.isEmpty(url)) {
            LISTENERS.remove(url);
        }
    }

    public static ProgressListener getListener(String url) {
        url = getUrlNoParams(url);
        WeakReference<ProgressListener> weakReference = LISTENERS.get(url);
        if (weakReference != null && weakReference.get() != null) {
            return weakReference.get();
        }
        return null;
    }

    public static String getUrlNoParams(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String[] strings = url.split("[?]");
        return strings[0];
    }

    public static String getImageName(String url){
        String urlNoParams = getUrlNoParams(url);
        int startIndex = urlNoParams.lastIndexOf("/");
        startIndex = Math.max(startIndex, 0);
        return urlNoParams.substring(startIndex);
    }

    public ProgressInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (chain.call().isCanceled()) {
            throw new IOException("Canceled");
        }
        Request request = chain.request();
        Response response = chain.proceed(request);

        String url = request.url().toString();
        final ProgressListener listener = getListener(url);
        if (listener == null) {
            removeListener(url);//实际是移除弱引用
            return response;
        } else {
            boolean process = !TextUtils.isEmpty(request.headers().get(PROGRESS_INTERCEPTOR_KEY));
            if (process) {
                ResponseBody body = response.body();
                response = response.newBuilder().body(new ProgressResponseBody(request, body)).build();
            }
        }


        if (response.code() / 100 == 4 || response.code() / 100 == 5) {                                                   // http code = 40X or 50X 直接报错
            removeListener(url);
            throw new HttpException(response.message(), response.code());
        }
        return response;
    }


}
