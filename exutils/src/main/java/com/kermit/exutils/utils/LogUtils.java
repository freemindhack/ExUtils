package com.kermit.exutils.utils;

import android.util.Log;

/**
 * Created by Kermit on 15-9-20.
 * e-mail : wk19951231@163.com
 */

public class LogUtils {

    public static boolean DEBUG = false;

    public static void v(String tag, String msg){
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg){
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }
}
