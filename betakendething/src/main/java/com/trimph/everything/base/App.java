package com.trimph.everything.base;

import android.app.Application;

/**
 * author: Trimph
 * data: 2016/12/16.
 * description:
 */

public class App extends Application {

    public App app;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static  App getInstance(){
        return new App();
    }

}
