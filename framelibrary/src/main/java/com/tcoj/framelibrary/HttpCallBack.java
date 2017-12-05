package com.tcoj.framelibrary;

import android.content.Context;

import com.google.gson.Gson;
import com.tcoj.baselibrary.http.EngineCallBack;
import com.tcoj.baselibrary.http.HttpUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/5 0005.
 * 添加共用参数
 */

public abstract class HttpCallBack<T> implements EngineCallBack{
    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        params.put("appkey","76253a4c8656d647");
        //参数拼接完成，开始执行
        onPreExecute();
    }

    public  void onPreExecute() {

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();

        T mResult = (T) gson.fromJson(result, HttpUtils.analysisClazzInfo(this));
        onSuccess(mResult);

    }
    //返回可以直接操作的对象
    public abstract void onSuccess(T result);
}
