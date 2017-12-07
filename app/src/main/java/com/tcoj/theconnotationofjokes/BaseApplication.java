package com.tcoj.theconnotationofjokes;

import android.app.Application;


import com.tcoj.baselibrary.ExceptionCrashHandler;
import com.tcoj.baselibrary.fixbug.FixDexManager;
import com.tcoj.baselibrary.http.HttpUtils;
import com.tcoj.framelibrary.http.OkHttpEngine;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        HttpUtils.init(new OkHttpEngine());
        //设置全局异常捕捉类
        /*ExceptionCrashHandler.getInstance().init(this);
        try {
            FixDexManager fixDexManager = new FixDexManager(this);
            fixDexManager.loadFixDex();
        }catch (Exception e){
            e.printStackTrace();
        }*/


    }
}
