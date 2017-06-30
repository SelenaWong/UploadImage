package com.wezley.uploadImage.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;


import com.squareup.leakcanary.LeakCanary;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by wezley on 2017/6/3.
 */
public class App extends Application {




    @Override
    public void onCreate() {
        super.onCreate();

     /*   if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
    }


}
