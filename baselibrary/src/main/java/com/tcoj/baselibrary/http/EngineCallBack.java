package com.tcoj.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public interface EngineCallBack {
    //执行之前，调用这个方法
    void onPreExecute(Context context, Map<String,Object> params);

    //失败
    void onError(Exception e);
    //成功
    void onSuccess(String result);
    //默认回调
    public final EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
