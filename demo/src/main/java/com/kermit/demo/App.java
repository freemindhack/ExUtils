package com.kermit.demo;

import android.app.Application;

import com.kermit.exutils.utils.ExUtils;

/**
 * Created by Kermit on 15-8-26.
 * e-mail : wk19951231@163.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ExUtils.initialize(this);
        ExUtils.setDebug(true);
    }
}
