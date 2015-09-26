package com.kermit.exutils.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kermit.exutils.model.ModelManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Kermit on 15-8-14.
 * e-mail : wk19951231@163.com
 */
public class ExUtils {

    private static Context mApplicationContext;

    private ExUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void setDebug(boolean isDebug){
        LogUtils.DEBUG = isDebug;
    }

    public static void initialize(Application app){
        mApplicationContext = app.getApplicationContext();
        ModelManager.init(mApplicationContext);
    }

    public static int dip2dx(float dpValue){
        float scale = mApplicationContext.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale + 0.5F);
    }

    public static int px2dip(float pxValue){
        float scale = mApplicationContext.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale + 0.5F);
    }


    public static int getScreenWidth(){
        DisplayMetrics metrics = mApplicationContext.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight(){
        DisplayMetrics metrics = mApplicationContext.getResources().getDisplayMetrics();
        return metrics.heightPixels - getStatusBarHeight();
    }

    public static int getStatusBarHeight(){
        int result = 0;
        int resourceId = mApplicationContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0) {
            result = mApplicationContext.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }


    public static SharedPreferences getSharedPreference() {
        return mApplicationContext.getSharedPreferences(mApplicationContext.getPackageName(), 0);
    }

    /**
     * 计算距离
     * @param longtitude1
     * @param latitude1
     * @param longtitude2
     * @param latitude2
     * @return
     */
    public static double distance(double longtitude1, double latitude1, double longtitude2, double latitude2) {
        double R = 6378137.0D;
        latitude1 = latitude1 * 3.141592653589793D / 180.0D;
        latitude2 = latitude2 * 3.141592653589793D / 180.0D;
        double a = latitude1 - latitude2;
        double b = (longtitude1 - longtitude2) * 3.141592653589793D / 180.0D;
        double sa2 = Math.sin(a / 2.0D);
        double sb2 = Math.sin(b / 2.0D);
        double d = 2.0D * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(latitude1) * Math.cos(latitude2) * sb2 * sb2));
        return d;
    }

    public static void Toast(String text){
        Toast.makeText(mApplicationContext, text, Toast.LENGTH_SHORT).show();
    }

    public static void ToastLong(String text){
        Toast.makeText(mApplicationContext, text, Toast.LENGTH_LONG).show();
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    /**]
     * 获取应用版本号
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager pm=context.getPackageManager();
        try {
            PackageInfo info=pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开浏览器
     * @param context
     * @param urlText
     */
    public static void openBrowser(Context context, String urlText) {
        Intent intent=new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url=Uri.parse(urlText);
        intent.setData(url);
        context.startActivity(intent);
    }

    /**
     * 隐藏标题栏
     * @param activity
     */
    public static void hideTitleBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    /**
     * 全屏
     * @param activity
     * @param isFull
     */
    public static void toggleFullScreen(Activity activity, boolean isFull) {
        hideTitleBar(activity);
        Window window=activity.getWindow();
        WindowManager.LayoutParams params=window.getAttributes();
        if(isFull) {
            params.flags|=WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            params.flags&=(~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(params);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 强制设置activity为垂直方向
     * @param activity
     */
    public static void setScreenVertical(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    /**
     * 安装一个apk
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent=new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 将输入流中的数据全部读取出来, 一次性返回
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] load(InputStream is) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len;
        while((len=is.read(buffer)) != -1)
            baos.write(buffer, 0, len);
        baos.close();
        is.close();
        return baos.toByteArray();
    }


    /**
     * 根据文件得到字节流
     * @param file
     * @return
     */
    public static byte[] getFileByte(File file) {
        if(!file.exists()) {
            return null;
        }
        try {
            FileInputStream fis=new FileInputStream(file);
            int len=fis.available();
            byte[] bytes=new byte[len];
            fis.read(bytes);
            fis.close();
            return bytes;
        } catch(Exception e) {

        }
        return null;
    }


    /**
     * 大数组（String）获取相同元素 大致思路是:1.首先将两个数组A、B排序(递增)<br>
     * 2.分别从A和B中各取出一元素a,b，对a和b进行比 较：<br>
     * 1) 如果a与b相等，则将a或b存入一指定集合中<br>
     * 2)如果a小于b，则继续取A的下一元素，再与b比 较<br>
     * 3) 如果a大于b，则取B的下一个元素，与a进行比较<br>
     * 3.反复进行步骤2，知道A或B的元素都比较完<br>
     * 4.返回集合(存了相同的元素)<br>
     * @param strArr1
     * @param strArr2
     * @return
     */
    public static List<String> getAllSameElement2(String[] strArr1, String[] strArr2) {
        if(strArr1 == null || strArr2 == null) {
            return null;
        }
        Arrays.sort(strArr1);
        Arrays.sort(strArr2);
        List<String> list=new ArrayList<String>();
        int k=0;
        int j=0;
        while(k < strArr1.length && j < strArr2.length) {
            if(strArr1[k].compareTo(strArr2[j]) == 0) {
                if(strArr1[k].equals(strArr2[j])) {
                    list.add(strArr1[k]);
                    k++;
                    j++;
                }
                continue;
            } else if(strArr1[k].compareTo(strArr2[j]) < 0) {
                k++;
            } else {
                j++;
            }
        }
        return list;
    }


    /**
     * 获取当前的年、月、日 对应的时间
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTime() {
        Date d=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateNowStr=sdf.format(d);
        // System.out.println("格式化后的日期：" + dateNowStr);
        return dateNowStr;
    }
}