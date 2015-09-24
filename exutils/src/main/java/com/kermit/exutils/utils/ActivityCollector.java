package com.kermit.exutils.utils;

import android.app.Activity;
import android.text.TextUtils;

import java.util.LinkedList;

/**
 * Created by Kermit on 15-9-4.
 * e-mail : wk19951231@163.com
 */
public class ActivityCollector {

    private static LinkedList<Activity> activityStack;
    private static ActivityCollector instance;


    public static ActivityCollector getInstance(){
        if (instance == null){
            instance = new ActivityCollector();
        }
        return instance;
    }

    public Activity currentActivity(){
        Activity activity = null;
        if (activityStack != null && !activityStack.isEmpty()){
            activity = activityStack.get(activityStack.size() - 1);
        }
        return activity;
    }

    public void pushActivity(Activity activity){
        if (activityStack == null){
            activityStack = new LinkedList<>();
        }
        activityStack.add(activity);
    }

    public void popActivity(Activity activity){
        activityStack.remove(activity);
    }

    public void closeActivity(Activity activity){
        if (activity != null){
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void closeAllActivity(){
        while(true){
            Activity activity = currentActivity();
            if (null == activity){
                break;
            }
            closeActivity(activity);
        }
    }

    public void closeActivityByName(String name){
        while (true){
            Activity activity = currentActivity();
            if (null == activity){
                break;
            }

            String activityName = activity.getComponentName().getClassName().toString();
            if (TextUtils.equals(activityName, name)){
                popActivity(activity);
                closeActivity(activity);
                break;
            }else{
                popActivity(activity);
            }
        }
    }

    public String getCurrentActivityName(){
        String name = "";
        Activity activity = currentActivity();
        if (activity != null){
            name = activity.getComponentName().getClassName().toString();
        }
        return name;
    }

}
