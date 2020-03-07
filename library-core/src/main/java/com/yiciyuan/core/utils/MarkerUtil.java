package com.yiciyuan.core.utils;

import android.os.Build;

import java.util.Locale;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2019-12-20 11:08
 * Description:
 */
public class MarkerUtil {

    private static final String MANUFACTURER_HUAWEI = "huawei";//华为
    private static final String MANUFACTURER_MEIZU = "meizu";//魅族
    private static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    private static final String MANUFACTURER_SONY = "sony";//索尼
    private static final String MANUFACTURER_OPPO = "oppo";
    private static final String MANUFACTURER_LG = "lg";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    private static final String MANUFACTURER_LETV = "letv";//乐视
    private static final String MANUFACTURER_ZTE = "zte";//中兴
    private static final String MANUFACTURER_YULONG = "yulong";//酷派
    private static final String MANUFACTURER_LENOVO = "lenovo";//联想

    private static String getVendor() {
        String vendor = Build.MANUFACTURER;
        return vendor.toLowerCase(Locale.ENGLISH);
    }

    public static boolean isHuawei() {
        if (getVendor().equals(MANUFACTURER_HUAWEI)) {
            return true;
        }
        return false;
    }

    public static boolean isXiaomi() {
        if (getVendor().equals(MANUFACTURER_XIAOMI)) {
            return true;
        }
        return false;
    }
}
