package com.example.kernel.net.result;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2019-07-15 11:24
 * Description:
 */
public class HttpListResult<T> implements Serializable {

    public int total;
    public int pages;
    public T list;
}
