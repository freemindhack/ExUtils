package com.kermit.exutils.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by kermit on 15-11-25.
 */
public class FileUtils {

    private static Context mApplicationContext;


    public static void init(Context context){
        mApplicationContext = context;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获取缓存地址
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = mApplicationContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mApplicationContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}
