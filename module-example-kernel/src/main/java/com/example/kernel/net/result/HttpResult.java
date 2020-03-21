package com.example.kernel.net.result;

import com.yiciyuan.annotation.apt.ApiConfig;

@ApiConfig(resultCode = "resCode1", resultMsg = "resMsg1", resultData = "data1")
public class HttpResult<T> {
    public int resCode1;
    public String resMsg1;
    public T data1;
}
