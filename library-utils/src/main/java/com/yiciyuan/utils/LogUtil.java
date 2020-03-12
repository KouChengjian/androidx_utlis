package com.yiciyuan.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 自定义Log工具类
 *
 * @author zhangyb@ifenguo.com
 * @createDate 2014年11月27日
 */
public class LogUtil {

    private static int v = 0;
    private static int d = 1;
    private static int i = 2;
    private static int w = 3;
    private static int e = 4;
    private static int TAG = -1;

    public static String customTagPrefix = "x_log";

    private LogUtil() {
    }

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void v(String tag, Object msg) {
        if (v > TAG) {
            Log.v(tag, "" + msg);
        }
    }

    public static void d(String tag, Object msg) {
        if (d > TAG) {
            Log.d(tag, "" + msg);
        }
    }

    public static void i(String tag, Object msg) {
        if (i > TAG) {
            Log.i(tag, "" + msg);
        }
    }

    public static void w(String tag, Object msg) {
        if (w > TAG) {
            Log.w(tag, "" + msg);
        }
    }

    public static void e(String tag, Object msg) {
        if (e > TAG) {
            Log.e(tag, "" + msg);
        }
    }

    public static void simpleLog(Object msg) {
        if (e > TAG) {
            Log.e("LogUtil", "" + msg);
        }
    }

    public static void d(String content) {
        String tag = generateTag();

        Log.d(tag, content);
    }

    public static void d(String content, Throwable tr) {
        String tag = generateTag();

        Log.d(tag, content, tr);
    }

    public static void e(String content) {
        String tag = generateTag();

        Log.e(tag, content);
    }

    public static void e(String content, Throwable tr) {
        String tag = generateTag();

        Log.e(tag, content, tr);
    }

    public static void e(String tag, String content) {
        Log.e(tag, content);
    }

    public static void i(String content) {
        String tag = generateTag();

        Log.i(tag, content);
    }

    public static void i(String content, Throwable tr) {
        String tag = generateTag();

        Log.i(tag, content, tr);
    }

    public static void v(String content) {
        String tag = generateTag();

        Log.v(tag, content);
    }

    public static void v(String content, Throwable tr) {
        String tag = generateTag();

        Log.v(tag, content, tr);
    }

    public static void w(String content) {
        String tag = generateTag();

        Log.w(tag, content);
    }

    public static void w(String content, Throwable tr) {
        String tag = generateTag();

        Log.w(tag, content, tr);
    }

    public static void w(Throwable tr) {
        String tag = generateTag();

        Log.w(tag, tr);
    }


    public static void wtf(String content) {
        String tag = generateTag();

        Log.wtf(tag, content);
    }

    public static void wtf(String content, Throwable tr) {
        String tag = generateTag();

        Log.wtf(tag, content, tr);
    }

    public static void wtf(Throwable tr) {
        String tag = generateTag();

        Log.wtf(tag, tr);
    }
}
