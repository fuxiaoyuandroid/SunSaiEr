package com.tcoj.baselibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Administrator on 2017/11/22 0022.
 * 整个应用的基本Activity
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setSelfContentView();
        //初始化头部
        initTitle();
        //初始化界面
        initView();
        //初始化数据
        initData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    /**
     * 设置布局
     */
    protected abstract void setSelfContentView();

    /**
     * 初始化头部
     */
    protected abstract void initTitle();

    /**
     * 初始化界面
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 页面跳转
     */
    protected void startActivity(Class<? extends Activity> activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }


}
