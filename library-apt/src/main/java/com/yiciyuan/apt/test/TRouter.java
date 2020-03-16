package com.yiciyuan.apt.test;

//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.example.kernel.ui.activity.MainActivity;
//import com.example.kernel.ui.activity.RecyclerActivity;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.util.BitSet;
//import java.util.HashMap;
//import java.util.Map;

//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.util.HashMap;
//import java.util.Map;

/**
 * @ 全局路由器 此类由apt自动生成
 */
//public final class TRouter {
//
//    private Context mContext;
//    private static BitSet IGNORE_ENCODING;
//    private static volatile TRouter mTRouter;
//
//    public TRouter() {
//        IGNORE_ENCODING = new BitSet(256);
//        int i;
//        for (i = 'a'; i <= 'z'; i++) {
//            IGNORE_ENCODING.set(i);
//        }
//        for (i = 'A'; i <= 'Z'; i++) {
//            IGNORE_ENCODING.set(i);
//        }
//        for (i = '0'; i <= '9'; i++) {
//            IGNORE_ENCODING.set(i);
//        }
//        IGNORE_ENCODING.set('*');
//        IGNORE_ENCODING.set('~');
//        IGNORE_ENCODING.set('!');
//        IGNORE_ENCODING.set('(');
//        IGNORE_ENCODING.set(')');
//        IGNORE_ENCODING.set('\'');
//    }
//
//    /**
//     * @此方法由apt自动生成
//     */
//    public static TRouter with(Context context) {
//        if (mTRouter == null) {
//            mTRouter = new TRouter();
//        }
//        mTRouter.mContext = context;
//        return mTRouter;
//    }
//
//    /**
//     * @此方法由apt自动生成
//     */
//    public void go(String url) {
//        if (url == null || url.isEmpty()) return;
//        if (!url.contains("?")) {
//            go(url, 0);
//            return;
//        }
//        String[] str = url.split("\\?");
//        String name = str[0];
//        String params = str[1];
//        Bundle bundle = new Bundle();
//        Map<String, String> hashMap = toMap(params);
//        for (String key : hashMap.keySet()) {
//            bundle.putString(key, hashMap.get(key));
//        }
//        go(name, bundle);
//    }
//
//    /**
//     * @此方法由apt自动生成
//     */
//    public void go(String name, int requestCode) {
//        go(name, null, requestCode);
//    }
//
//    /**
//     * @此方法由apt自动生成
//     */
//    public void go(String name, Bundle bundle) {
//        go(name, bundle, 0);
//    }
//
//    /**
//     * @此方法由apt自动生成
//     */
//    public void go(String name, Bundle bundle, int requestCode) {
//        if (name == null || name.isEmpty()) return;
//        switch (name) {
//            case "dasdsadsas":
//                starActivity(RecyclerActivity.class, bundle, requestCode);
//                break;
//            case "xxxx":
//                starActivity(MainActivity.class, bundle, requestCode);
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * @此方法由apt自动生成
//     */
//    private void starActivity(Class cls, Bundle bundle, int requestCode) {
//        Intent intent = new Intent(mContext, cls);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        if (requestCode != 0) {
//            ((Activity) mContext).startActivityForResult(intent, requestCode);
//        } else {
//            mContext.startActivity(intent);
//        }
//        mContext = null;
//    }
//
//    private String decode(String k) {
//        try {
//            return URLDecoder.decode(k, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    /**
//     * 判断c是否是16进制的字符
//     */
//    private Boolean isIgnore(char c) {
//        if (IGNORE_ENCODING.get((int) c)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private boolean hasEncodeURIComponent(String str) {
//        /**
//         * 支持JAVA的URLEncoder.encode出来的string做判断。 即: 将' '转成'+' <br>
//         * 0-9a-zA-Z保留 <br>
//         * '-'，'_'，'.'，'*'保留 <br>
//         * 其他字符转成%XX的格式，X是16进制的大写字符，范围是[0-9A-F]
//         */
//        boolean needEncode = false;
//        for (int i = 0; i < str.length(); i++) {
//            char c = str.charAt(i);
//            if (IGNORE_ENCODING.get((int) c)) {
//                continue;
//            }
//            if (c == '%' && (i + 2) < str.length()) {
//                // 判断是否符合urlEncode规范
//                char c1 = str.charAt(++i);
//                char c2 = str.charAt(++i);
//                if (isIgnore(c1) && isIgnore(c2)) {
//                    continue;
//                }
//            }
//            // 其他字符，肯定需要urlEncode
//            needEncode = true;
//            break;
//        }
//
//        return !needEncode;
//    }
//
//    private Map<String, String> toMap(String url) {
//        Map<String, String> map = new HashMap<>();
//        if (url != null && url.contains("=")) {
//            String[] arrTemp = url.split("&");
//            for (String str : arrTemp) {
//                String[] qs = str.split("=");
//                String s = qs[1];
//                if (hasEncodeURIComponent(s)) {
//                    s = decode(s);
//                }
//                map.put(qs[0], s);
//            }
//        }
//        return map;
//    }
//
//
//}
