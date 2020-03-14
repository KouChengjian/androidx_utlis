package com.example.net.result;

/**
 * 返回结果处理基类
 */

public class HttpResult<T> {
    public int resCode;
    public String resMsg;
    public T data;
}
