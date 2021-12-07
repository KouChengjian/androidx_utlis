package com.yiciyuan.kernel.utils.glide;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.yiciyuan.kernel.R;
import com.yiciyuan.kernel.utils.glide.transform.BlurTransformation;
import com.yiciyuan.kernel.utils.glide.transform.MatrixTransformation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class GlideUtil {

    public static class Ext {
        private static Application app;
        private static String referer = "";

        private Ext() {
        }

        public static void init(Application app) {
            if (Ext.app == null) {
                Ext.app = app;
            }
        }

        public static String getReferer() {
            return referer;
        }

        public static void setReferer(String ref) {
            referer = ref;
        }
    }

    private static RequestOptions mRequestOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.bg_default_image)
            .error(R.mipmap.bg_default_load_error);

    public static Context getContext() {
        Context context = Ext.app;
        if (context == null) {
            throw new RuntimeException("请初始化 GlideUtil.Ext.init");
        }
        return context;
    }

    public static Object getModel(String url) {
        final Map<String, String> header = new HashMap<>();
        return getModel(url, header);
    }

    public static Object getModel(String url, final Map<String, String> headerMap) {
        if (TextUtils.isEmpty(url)) return url;
        if (url.startsWith("/")) return url;
        headerMap.put("referer", Ext.getReferer());
        Headers headers = new Headers() {
            @Override
            public Map<String, String> getHeaders() {
                return headerMap;
            }
        };
        return new GlideUrlNoParams(url, headers);
    }

    public static Object getModel2(String url, final Map<String, String> headerMap) {
        if (TextUtils.isEmpty(url)) return url;
        if (url.startsWith("/")) return url;
        headerMap.put("referer", Ext.getReferer());
        Headers headers = new Headers() {
            @Override
            public Map<String, String> getHeaders() {
                return headerMap;
            }
        };
        return new GlideUrl(url, headers);
    }

    public static void pause(Context context) {
        getRequestManager().pauseRequests();
    }

    public static void resume(Context context) {
        getRequestManager().resumeRequests();
    }

    public static Glide getGlideWithContext() {
        return Glide.get(getContext());
    }

    public static RequestManager getRequestManager() {
        return getRequestManager(getContext());
    }

    public static RequestManager getRequestManager(Context context) {
        return Glide.with(context);
    }

    public static RequestOptions getDefaultRequestOptions() {
        return mRequestOptions.clone();
    }

    public static RequestBuilder<Bitmap> loadAsBitMap(String url, Map<String, String> headerMap) {
        return getRequestManager().asBitmap().load(getModel(url, headerMap));
    }

    public static RequestBuilder<Bitmap> loadAsBitMap(Context context, String url, Map<String, String> headerMap) {
        return getRequestManager(context).asBitmap().load(getModel(url, headerMap));
    }

    public static RequestBuilder<GifDrawable> loadAsGif(String url, Map<String, String> headerMap) {
        return getRequestManager().asGif().load(getModel(url, headerMap));
    }

    public static RequestBuilder<GifDrawable> loadAsGif(Context context, String url, Map<String, String> headerMap) {
        return getRequestManager(context).asGif().load(getModel(url, headerMap));
    }

    public static RequestBuilder<Drawable> loadAsDrawable(String url) {
        return getRequestManager().load(getModel(url));
    }

    public static RequestBuilder<Drawable> loadAsDrawable(Context context, String url) {
        return getRequestManager(context).load(getModel(url));
    }

    public static RequestBuilder<Drawable> loadAsDrawable(String url, Map<String, String> headerMap) {
        return getRequestManager().load(getModel(url, headerMap));
    }

    public static RequestBuilder<Drawable> loadAsDrawable(Context context, String url, Map<String, String> headerMap) {
        return getRequestManager(context).load(getModel(url, headerMap));
    }

    private static RequestBuilder<Bitmap> loadAsBitmap(String url) {
        return getRequestManager().asBitmap().load(getModel(url));
    }

    private static RequestBuilder<Bitmap> loadAsBitmap(Context context, String url) {
        return getRequestManager(context).asBitmap().load(getModel(url));
    }

    /**
     * 使用clearMemory有条件的，因为GlideUtil 使用的是全局的mContext,释放的时候，会释放所有占用的内存
     * This method must be called on the main thread.
     */
    public static void clearMemory() {
        Glide.get(getContext()).clearMemory();
    }

    /**
     * 清除磁盘缓存   // This method must be called on a background thread.
     */
    public static void clearDiskCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getContext()).clearDiskCache();
            }
        }).start();
    }

    /**
     * ------------------------------- 图片加载 -------------------------------------------
     */

    //预加载图片
    public static void preload(String url) {
        preload(url, new HashMap<String, String>(), null);
    }

    public static void preload(String url, final Map<String, String> headerMap) {
        preload(url, headerMap, null);
    }

    public static void preload(String url, final Map<String, String> headerMap, RequestListener<File> listener) {
        preload(getContext(), url, headerMap, listener);
    }

    public static void preload(Context context, String url) {
        preload(context, url, new HashMap<String, String>(), null);
    }

    public static void preload(Context context, String url, final Map<String, String> headerMap) {
        preload(context, url, headerMap, null);
    }

    public static void preload(Context context, String url, final Map<String, String> headerMap, RequestListener<File> listener) {
        RequestBuilder<File> builder = Glide.with(context).downloadOnly().load(getModel(url, headerMap));
        builder.addListener(listener);
        builder.preload();
    }

    public static void load(String url, ImageView imageView) {
        load(getContext(), url, imageView);
    }

    public static void load(Context context, String url, ImageView imageView) {
        load(context, url, imageView, 0, 0);
    }

    public static void load(String url, ImageView imageView, int placeholderResId) {
        load(getContext(), url, imageView, placeholderResId, 0);
    }

    public static void load(Context context, String url, ImageView imageView, int placeholderResId) {
        load(context, url, imageView, placeholderResId, 0);
    }

    public static void load(String url, ImageView imageView, int placeholderResId, int errorResId) {
        load(getContext(), url, imageView, placeholderResId, errorResId);
    }

    public static void load(Context context, String url, ImageView imageView, int placeholderResId, int errorResId) {
        RequestOptions requestOptions = getDefaultRequestOptions();
        if (placeholderResId != 0) {
            requestOptions.placeholder(placeholderResId);
        }
        if (errorResId != 0) {
            requestOptions.error(errorResId);
        }
        loadAsDrawable(context, url)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void load(String url, ImageView imageView, int placeholderResId, int errorResId, RequestListener requestCallBack) {
        RequestOptions requestOptions = getDefaultRequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId);
        loadAsDrawable(url)
                .apply(requestOptions)
                .listener(requestCallBack)
                .into(imageView);
    }

    public static void load(int resId, ImageView imageView) {
        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory
                .Builder(300)
                .setCrossFadeEnabled(true)
                .build();
        RequestOptions requestOptions = getDefaultRequestOptions();
        getRequestManager()
                .load(resId)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade(drawableCrossFadeFactory))
                .into(imageView);
    }

    //加载图片为drawable并回调
    public static void loadDrawableToTarget(String url, CustomViewTarget target) {
        loadAsDrawable(url).into(target);
    }

    public static void loadDrawableToTarget(String url, Map<String, String> headerMap, CustomViewTarget target) {
        loadAsDrawable(url, headerMap).into(target);
    }

    //加载图片为bitmap并回调
    public static void loadBitmapToTarget(String url, BitmapImageViewTarget target) {
        RequestOptions requestOptions = getDefaultRequestOptions();
        loadAsBitmap(url)
                .apply(requestOptions)
                .into(target);
    }

    public static void loadBitmapToTarget(String url, SimpleTarget<Bitmap> target) {
        RequestOptions requestOptions = getDefaultRequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888);
        loadAsBitmap(url)
                .apply(requestOptions)
                .into(target);
    }

    public static void loadGifToTarget(final String path, final ImageView gifView) {
        loadGifToTarget(path, gifView, DiskCacheStrategy.ALL);
    }

    public static void loadGifToTarget(final String path, final ImageView gifView, DiskCacheStrategy diskCacheStrategy) {
        RequestOptions requestOptions = getDefaultRequestOptions().diskCacheStrategy(diskCacheStrategy).priority(Priority.HIGH);

        getRequestManager()
                .asGif()
                .load(path)
                .apply(requestOptions)
                //imageView即 PhotoView对象
                .into(gifView);
    }

    public static void loadGifToTarget(final int rid, final ImageView gifView) {
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH);
        getRequestManager()
                .asGif()
                .load(rid)
                .apply(requestOptions)
                //imageView即 PhotoView对象
                .into(gifView);
    }

    //加载圆型的图片
    public static void loadCirclePicture(String url, ImageView imageView) {
        RequestOptions requestOptions = getDefaultRequestOptions()
                .bitmapTransform(new CircleCrop());
        getRequestManager()
                .load(getModel(url))
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadCirclePicture(int resId, ImageView imageView) {
        RequestOptions requestOptions = getDefaultRequestOptions()
                .bitmapTransform(new CircleCrop());
        getRequestManager()
                .load(resId)
                .apply(requestOptions)
                .into(imageView);
    }

    //加载圆角图片
    public static void loadRoundPicture(String url, ImageView imageView) {
        ViewGroup.LayoutParams p = imageView.getLayoutParams();
        int corners = (int) (0.15f * Math.min(p.width, p.height));
//        corners = Math.max(corners, getContext().getResources().getDimensionPixelOffset(R.dimen.px6dp));
        loadRoundPicture(url, imageView, corners);
    }

    public static void loadRoundPictureNoSan(String url, ImageView imageView) {
        ViewGroup.LayoutParams p = imageView.getLayoutParams();
        int corners = (int) (0.15f * Math.min(p.width, p.height));
//        corners = Math.max(corners, getContext().getResources().getDimensionPixelOffset(R.dimen.px6dp));
        if (corners == 0) {
//            corners = Math.max(corners, getContext().getResources().getDimensionPixelOffset(R.dimen.px6dp));
        }
        RequestOptions requestOptions = getDefaultRequestOptions()
                .bitmapTransform(new RoundedCorners(corners));
        getRequestManager()
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }


    public static void loadRoundPictureNoSan(int mipmap, ImageView imageView) {
        ViewGroup.LayoutParams p = imageView.getLayoutParams();
        int corners = (int) (0.15f * Math.min(p.width, p.height));
//        corners = Math.max(corners, getContext().getResources().getDimensionPixelOffset(R.dimen.px6dp));
        if (corners == 0) {
//            corners = Math.max(corners, getContext().getResources().getDimensionPixelOffset(R.dimen.px6dp));
        }
        RequestOptions requestOptions = getDefaultRequestOptions()
                .bitmapTransform(new RoundedCorners(corners));
        getRequestManager()
                .load(mipmap)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadRoundPicture(String url, ImageView imageView, int corners) {
        if (corners == 0) {
//            corners = Math.max(corners, getContext().getResources().getDimensionPixelOffset(R.dimen.px6dp));
        }
        RequestOptions requestOptions = getDefaultRequestOptions()
                .bitmapTransform(new RoundedCorners(corners));
        loadAsDrawable(url)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadRoundPicture(Drawable res, ImageView imageView) {
        ViewGroup.LayoutParams p = imageView.getLayoutParams();
        int corners = (int) (0.15f * Math.min(p.width, p.height));
//        corners = Math.max(corners, getContext().getResources().getDimensionPixelOffset(R.dimen.px6dp));
        loadRoundPicture(res, imageView, corners);
    }

    public static void loadRoundPicture(int res, ImageView imageView) {
        ViewGroup.LayoutParams p = imageView.getLayoutParams();
        int corners = (int) (0.15f * Math.min(p.width, p.height));
//        corners = Math.max(corners, getContext().getResources().getDimensionPixelOffset(R.dimen.px6dp));
        loadRoundPicture(res, imageView, corners);
    }

    public static void loadRoundPicture(int res, ImageView imageView, int corners) {
        if (corners == 0) {
//            corners = Math.max(corners, getContext().getResources().getDimensionPixelOffset(R.dimen.px6dp));
        }
        RequestOptions requestOptions = getDefaultRequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new RoundedCorners(corners));
        getRequestManager()
                .load(res)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadRoundPicture(Drawable res, ImageView imageView, int corners) {
        if (corners == 0) {
//            corners = Math.max(corners, getContext().getResources().getDimensionPixelOffset(R.dimen.px6dp));
        }
        RequestOptions requestOptions = getDefaultRequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new RoundedCorners(corners));
        getRequestManager()
                .load(res)
                .apply(requestOptions)
                .into(imageView);
    }

    //加载高斯模糊图片
    public static void loadBlurPicture(String url, int placeholder, int error, ImageView imageView) {

        if (imageView == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(placeholder);
        } else {
            RequestOptions requestOptions;
            if (placeholder != R.mipmap.bg_default_image || error != R.mipmap.bg_default_image) {
                requestOptions = new RequestOptions().placeholder(placeholder).error(error);
            } else {
                requestOptions = getDefaultRequestOptions();
            }
            requestOptions = requestOptions.dontAnimate().bitmapTransform(new BlurTransformation(getContext(), 20, 1));// “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
            loadAsDrawable(url)
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    //特殊加载
    public static void loadPicture(String url, int placeholder, int error, final Map<String, String> header, DrawableImageViewTarget listener) {
        Headers headers = new Headers() {
            @Override
            public Map<String, String> getHeaders() {
                return header;
            }
        };
        RequestBuilder<Drawable> builder = getRequestManager().load(new GlideUrlNoParams(url, headers));
        RequestOptions requestOptions;
        if (placeholder != R.mipmap.bg_default_image || error != R.mipmap.bg_default_image) {
            requestOptions = new RequestOptions()
                    .placeholder(placeholder)
                    .error(error);
        } else {
            requestOptions = getDefaultRequestOptions();
        }
        requestOptions = requestOptions.dontAnimate();

        builder.apply(requestOptions).into(listener);
    }

    //加载图片为width = 100%, height = auto
    public static void loadMatrixPicture(String url, ImageView imageView) {
        MatrixTransformation transformation = new MatrixTransformation(imageView);
        RequestOptions requestOptions = getDefaultRequestOptions().placeholder(null);
        loadAsBitmap(url)
                .apply(requestOptions)
                .into(transformation);
    }
}
