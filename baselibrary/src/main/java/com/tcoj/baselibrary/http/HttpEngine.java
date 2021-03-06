package com.tcoj.baselibrary.http;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/4 0004.
 * 引擎规范
 */

public interface HttpEngine {

    void get(boolean cache,Context context, String url, HashMap<String,Object> params, EngineCallBack callBack);

    void post(boolean cache,Context context, String url,HashMap<String,Object> params,EngineCallBack callBack);
}
