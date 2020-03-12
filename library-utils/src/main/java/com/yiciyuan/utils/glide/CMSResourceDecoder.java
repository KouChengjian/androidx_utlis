package com.yiciyuan.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Registry;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.Downsampler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CMSResourceDecoder implements ResourceDecoder<ByteBuffer, Bitmap> {


    private Downsampler downsampler;

    @Override
    public boolean handles(@NonNull ByteBuffer source, @NonNull Options options) throws IOException {

//        if (ImageEncryptUtil.isEncryptImage(source)) {
//            return true;
//        }
        return false;
    }

    @Nullable
    @Override
    public Resource<Bitmap> decode(@NonNull ByteBuffer source, int width, int height, @NonNull Options options) throws IOException {

        if (downsampler == null) {
            Context context = GlideUtil.getGlideWithContext().getContext();
            Registry registry = GlideUtil.getGlideWithContext().getRegistry();
            ArrayPool arrayPool = GlideUtil.getGlideWithContext().getArrayPool();
            BitmapPool bitmapPool = GlideUtil.getGlideWithContext().getBitmapPool();
            downsampler = new Downsampler(registry.getImageHeaderParsers(), context.getResources().getDisplayMetrics(), bitmapPool, arrayPool);
        }
        byte[] buffer = new byte[source.remaining()];
        source.get(buffer);
        InputStream stream =null; //ImageEncryptUtil.decodeImage(buffer);
        //LogUtil.e("wzy0524", "decode");
        return downsampler.decode(stream, width, height, options);
    }
}
