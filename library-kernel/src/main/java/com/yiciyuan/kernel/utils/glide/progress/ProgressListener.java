package com.yiciyuan.kernel.utils.glide.progress;

/**
 * 描述:
 * <p>
 * 进度的的监听
 * Created by allens on 2018/1/8.
 */
public interface ProgressListener {

    void onProgress(String url, long bytesRead, long contentLength);
}
