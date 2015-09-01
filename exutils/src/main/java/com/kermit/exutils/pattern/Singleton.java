package com.kermit.exutils.pattern;

/**
 * Created by Kermit on 15-8-31.
 * e-mail : wk19951231@163.com
 */
public abstract class Singleton<T> {

    private T mInstance;

    protected abstract T create();

    public final T getInstance(){
        synchronized (this){
            if (mInstance == null){
                mInstance = create();
            }
            return mInstance;
        }
    }

}
