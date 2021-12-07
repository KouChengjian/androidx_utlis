package com.yiciyuan.kernel.utils;

import static android.os.Environment.MEDIA_MOUNTED;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2018/8/24 10:00
 * Description:
 */
public class CacheUtil {

    private static final String TAG = "CacheUtils";
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    public CacheUtil() {
        // /data/data/包名/
        // context.getFilesDir();    // /data/data/包名/files
        // context.getCacheDir();    // /data/data/包名/cache

        // /sdcard/Android/data/包名/
//         context.getExternalFilesDir();    // /sdcard/Android/data/包名/files
        // context.getExternalCacheDir();    // /sdcard/Android/data/包名/cache

        // /sdcard/xxx
        // /storage/emulated/0
        // Environment.getExternalStorageDirectory();

        // /storage/emulated/0/DCIM, 另外还有MOVIE/MUSIC等很多种标准路径
        // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }

    /**
     * 内部存储文件目录路径
     * @param context
     * @return
     */
    public static File getFileDirectory(Context context) {
        File appCacheDir = null;
        if (appCacheDir == null) {
            appCacheDir = context.getFilesDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/files/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    /**
     * 内部存储缓存目录路径
     * @param context
     * @return
     */
    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    /**
     * 外部存储缓存目录路径
     * @param context
     * @param dirName
     * @return
     */
    public static File getExternalCacheDirectory(Context context, String dirName) {
        return getExternalCacheDirectory(context, true, dirName);
    }

    public static File getExternalCacheDirectory(Context context, boolean preferExternal, String dirName) {
        File appCacheDir = null;
        if (preferExternal && MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context, dirName);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            //Log.w("Can't define system cache directory! '%s' will be used.",cacheDirPath);
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context, String dirName) {
        File dataDir = context.getExternalCacheDir();
        File appCacheDir = new File(dataDir, dirName);
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                Log.e(TAG, "Unable to create external cache directory");
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
