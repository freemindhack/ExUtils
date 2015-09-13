package com.kermit.exutils.utils;

/**
 * Created by Kermit on 15-8-26.
 * e-mail : wk19951231@163.com
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//跟网络相关的工具类
public class NetUtils {

    private NetUtils() {
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        // 获取手机所有连接管理对象（包括wifi，net等连接的管理）
        ConnectivityManager connectivity=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null) {
            // 获取网络连接管理的对象
            NetworkInfo info=connectivity.getActiveNetworkInfo();
            if(info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if(info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

}