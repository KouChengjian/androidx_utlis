package com.yiciyuan.utils.glide.progress;


import java.io.IOException;

import androidx.annotation.Nullable;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2019/4/1 20:09
 * Description:
 */
public class ProgressResponseBody extends ResponseBody {


    private static final String TAG = "XGlide";

    private BufferedSource mBufferedSource;

    private ResponseBody mResponseBody;


    private Request mRequest;

    public ProgressResponseBody(Request request, ResponseBody responseBody) {
        this.mRequest = request;
        this.mResponseBody = responseBody;
    }


    @Nullable
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(new ProgressSource(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private class ProgressSource extends ForwardingSource {

        private long mTotalBytesRead = 0;

        ProgressSource(Source source) {
            super(source);
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = super.read(sink, byteCount);
            long fullLength = mResponseBody.contentLength();

            if (bytesRead == -1) {
                mTotalBytesRead = fullLength;
            } else {
                mTotalBytesRead += bytesRead;
            }

            String url = mRequest.url().toString();
            ProgressListener progressListener = ProgressInterceptor.getListener(url);
            if (progressListener != null) {
                progressListener.onProgress(url, mTotalBytesRead, fullLength);
            }
            return bytesRead;
        }
    }
}
