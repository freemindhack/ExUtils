package com.kermit.exutils.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by Kermit on 15-9-13.
 * e-mail : wk19951231@163.com
 */
public class CustomViewUtils {

    public static int getMeasuredSize(int size, int measureSpec){

        int result = size;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            //由用户任意指定大小
            case View.MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            //选取最小的一个值，最大不超过父viewgroup指定的大小
            case View.MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
            //父viewgroup指定了一个确定的值
            case View.MeasureSpec.EXACTLY:
                result = specSize;
                break;

        }

        return result;
    }

    /**
     *
     * @param context
     * @return 系统slop常量
     */
    public int getSlop(Context context){
        return ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     *
     * @param context
     * @param s
     * @return 返回系统fling常量
     */
    public int getFling(Context context, String s){
        switch (s){
            default:
            case "max":
                return ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
            case "min":
                return ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        }
    }
}
