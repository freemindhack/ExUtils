package com.kermit.exutils.utils;

import android.util.Log;

import com.kermit.exutils.BuildConfig;

/**
 * Created by Kermit on 15-9-20.
 * e-mail : wk19951231@163.com
 */
public class LogUtils {

    private static String Tag;

    public static void setTag(String tag){
        Tag = tag;
    }

    public static void v(String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(Tag, msg);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(Tag, msg);
        }

    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(Tag, msg);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(Tag, msg);
        }

    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(Tag, msg);
        }
    }
}
