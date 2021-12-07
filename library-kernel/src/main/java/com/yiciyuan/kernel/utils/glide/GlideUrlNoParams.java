package com.yiciyuan.kernel.utils.glide;

import android.text.TextUtils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;

import java.net.URL;

public class GlideUrlNoParams extends GlideUrl {

    private String mCacheKey;

    public GlideUrlNoParams(URL url) {
        super(url);
    }

    public GlideUrlNoParams(String url) {
        super(url);
    }

    public GlideUrlNoParams(URL url, Headers headers) {
        super(url, headers);
    }

    public GlideUrlNoParams(String url, Headers headers) {
        super(url, headers);
    }

    @Override
    public String getCacheKey() {
        if (TextUtils.isEmpty(mCacheKey)) {
            String url = toStringUrl();
            if (url.contains("?")) {
                mCacheKey = url.substring(0, url.lastIndexOf("?"));
            } else {
                mCacheKey = url;
            }
        }
        return mCacheKey;
    }

    @Override
    public int hashCode() {
        return getCacheKey().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        } else if (o instanceof GlideUrl){
            return getCacheKey().equals(((GlideUrl) o).getCacheKey());
        }else if (o instanceof String){
            return getCacheKey().equals(o.toString());
        }
        return false;
    }
}
