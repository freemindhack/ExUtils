package com.kermit.exutils.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Created by Kermit on 15-8-26.
 * e-mail : wk19951231@163.com
 */

//跟App相关的辅助类
public class AppUtils {

    private static Context mApplicationContext;

    public static void init(Context context){
        mApplicationContext = context;
    }


    private AppUtils() {
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName() {
        try {
            PackageManager packageManager = mApplicationContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    mApplicationContext.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return mApplicationContext.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    public static String getVersionName() {
        try {
            PackageManager packageManager = mApplicationContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    mApplicationContext.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}