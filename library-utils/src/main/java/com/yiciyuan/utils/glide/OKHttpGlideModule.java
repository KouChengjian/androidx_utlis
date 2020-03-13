package com.yiciyuan.utils.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.yiciyuan.utils.CacheUtil;
import com.yiciyuan.utils.Util;
import com.yiciyuan.utils.glide.progress.ProgressInterceptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A {@link GlideModule} implementation to replace Glide's default
 * {@link java.net.HttpURLConnection} based {@link com.bumptech.glide.load.model.ModelLoader} with an OkHttp based
 * {@link com.bumptech.glide.load.model.ModelLoader}.
 * <p/>
 * <p>
 * If you're using gradle, you can include this module simply by depending on the aar, the module will be merged
 * in by manifest merger. For other build systems or for more more information, see
 *
 * </p>
 */
//@Excludes(value = OkHttpLibraryGlideModule.class)
@GlideModule
public class OKHttpGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        // Do nothing.//配置缓存
        //builder.setLogLevel(Log.VERBOSE);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                   @NonNull Registry registry) {

//        registry.prepend(ByteBuffer.class, Bitmap.class, new CMSResourceDecoder());
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlFactory());

    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }


    public static class OkHttpUrlFactory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final static int CONNECT_TIMEOUT = 100;
        private final static int READ_TIMEOUT = 100;
        private final static int WRITE_TIMEOUT = 100;

        private static final int MAX_OK_HTTP_CLIENT_COUNT = 10;
        private static int AUTO_INDEX = 0;
        private static volatile Call.Factory[] OK_HTTP_CLIENT;

        public OkHttpUrlFactory() {
        }

        @Override
        @NonNull
        public ModelLoader<GlideUrl, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new OkHttpUrlLoader();
        }

        @Override
        public void teardown() {
        }

        private Call getCall(GlideUrl url) {
            Call.Factory client = getInternalClient();
            Request.Builder requestBuilder = new Request.Builder().url(url.toStringUrl());
            for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
                String key = headerEntry.getKey();
                if ("referer".equals(key.toLowerCase())) {
                    continue;
                }
                requestBuilder.addHeader(key, headerEntry.getValue());
            }
//            requestBuilder.addHeader("referer", Constant.RefererType.INSTANCE.getReferer());
            Request request = requestBuilder.build();

            return client.newCall(request);
        }

        private synchronized Call.Factory getInternalClient() {

            if (OK_HTTP_CLIENT == null) {
                OK_HTTP_CLIENT = new Call.Factory[MAX_OK_HTTP_CLIENT_COUNT];

                File dir = CacheUtil.getExternalCacheDirectory(Util.INSTANCE.getContext(),"sy_img_cache");
                int cacheSize = 100 * 1024 * 1024; // 100 MiB
                Cache cache = new Cache(dir, cacheSize);

                for (int i = 0; i < MAX_OK_HTTP_CLIENT_COUNT; i++) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                        .addInterceptor(new ProgressInterceptor());

                    if (dir != null) {
                        builder.cache(cache);
                    }
                    OK_HTTP_CLIENT[i] = builder.build();
                }
            }

            ++AUTO_INDEX;
            return OK_HTTP_CLIENT[AUTO_INDEX % MAX_OK_HTTP_CLIENT_COUNT];
        }

        class OkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {
            @Override
            public boolean handles(@NonNull GlideUrl url) {
                return true;
            }

            @Override
            public LoadData<InputStream> buildLoadData(@NonNull GlideUrl model, int width, int height, @NonNull Options options) {
                return new LoadData<>(model, new OkHttpStreamFetcher(model));
            }
        }


        class OkHttpStreamFetcher implements DataFetcher<InputStream>, okhttp3.Callback {
            private final GlideUrl mGlideUrl;
            private volatile Call mCall;
            private DataCallback<? super InputStream> mCallback;

            OkHttpStreamFetcher(GlideUrl glideUrl) {
                mGlideUrl = glideUrl;
            }

            @Override
            public void loadData(@NonNull Priority priority, @NonNull final DataFetcher.DataCallback<? super InputStream> callback) {

                this.mCallback = callback;
                if(!checkGlideUrl(mGlideUrl)) {
                    if (this.mCallback != null) {
                        this.mCallback.onLoadFailed(new IllegalArgumentException("unexpected url: " + mGlideUrl.toString()));
                        this.mCallback = null;
                    }
                    Log.e("OKHttpGlideModule", "loadData: err url : " + mGlideUrl.toString());
                    return;
                }

                this.mCall = getCall(mGlideUrl);
                this.mCall.enqueue(this);
                //https://github.com/bumptech/glide/issues/2355
                //3.9.1后，修复了这个
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (this.mCallback == null || e == null) {
                    return;
                }
                this.mCallback.onLoadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (this.mCallback == null) {
                    return;
                }
                ResponseBody responseBody = response.body();
                if (response.isSuccessful() && responseBody != null) {
                    int contentLength = (int) responseBody.contentLength();
                    if (contentLength != -1) {
                        try {
                            this.mCallback.onDataReady(ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength));
                        } catch (Exception ex) {
                            this.mCallback.onLoadFailed(new HttpException(response.message(), response.code()));
                            response.close();
                        }
                    }
                } else {
                    this.mCallback.onLoadFailed(new HttpException(response.message(), response.code()));
                    response.close();
                }
            }

            @Override
            public void cleanup() {
                reset();
            }

            @Override
            public void cancel() {
                reset();
            }

            private void reset() {
                if (this.mCall != null) {
                    this.mCall.cancel();
                }
                this.mCallback = null;
            }

            @NonNull
            @Override
            public Class<InputStream> getDataClass() {
                return InputStream.class;
            }

            @NonNull
            @Override
            public DataSource getDataSource() {
                return DataSource.REMOTE;
            }

            private boolean checkGlideUrl(GlideUrl glideUrl) {
                String url = glideUrl.toStringUrl();
                if (url == null) {
                    return false;
                }

                // Silently replace web socket URLs with HTTP URLs.
                if (url.regionMatches(true, 0, "ws:", 0, 3)) {
                    url = "http:" + url.substring(3);
                } else if (url.regionMatches(true, 0, "wss:", 0, 4)) {
                    url = "https:" + url.substring(4);
                }

                HttpUrl parsed = HttpUrl.parse(url);
                if (parsed == null) {
                    return false;
                }
                return true;
            }
        }
    }
}