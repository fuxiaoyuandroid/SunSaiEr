package com.tcoj.baselibrary.http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class HttpUtils{
    //上下文
    private Context mContext;
    //get or post
    private int mType = GET_TYPE;

    private static final int GET_TYPE = 0x0011;
    private static final int POST_TYPE = 0x0012;

    private String mUrl;

    private boolean mCache = false;

    private static HttpEngine mHttpEngine = null;

    private HashMap<String,Object> mParams;

    protected HttpUtils(Context context){
        this.mContext = context;
        mParams = new HashMap<>();
    }

    //application中初始化引擎
    public static void init(HttpEngine httpEngine){
        mHttpEngine = httpEngine;
    }

    //切换网络引擎
    public HttpUtils exchangeEngine(HttpEngine httpEngine){
        this.mHttpEngine = httpEngine;
        return this;
    }

    //创建
    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }
    //get提交
    public HttpUtils get(){
        mType = GET_TYPE;
        return this;
    }
    //post提交
    public HttpUtils post(){
        mType = POST_TYPE;
        return this;
    }
    //url
    public HttpUtils isCache(boolean cache){
        this.mCache = cache;
        return this;
    }
    //url
    public HttpUtils url(String url){
        this.mUrl = url;
        return this;
    }
    //单个参数
    public HttpUtils addParam(String key,Object value){
        mParams.put(key,value);
        return this;
    }
    //多个参数
    public HttpUtils addParams(HashMap<String,Object> params){
        mParams.putAll(params);
        return this;
    }

    //添加回调
    public void execute(EngineCallBack callBack){
        
        callBack.onPreExecute(mContext,mParams);

        if (callBack == null){
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }
        //判断执行方法
        if (mType == GET_TYPE){
            get(mUrl,mParams,callBack);
        }else if (mType == POST_TYPE){
            post(mUrl,mParams,callBack);
        }
    }
    //添加空参数回调
    public void execute(){
       execute(null);
    }

    //调用引擎
    private void get(String url, HashMap<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mCache,mContext,url,params,callBack);
    }


    private void post(String url, HashMap<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mCache,mContext,url,params,callBack);
    }

    /**
     * 拼接参数
     */
    public static String jointParams(String url, HashMap<String, Object> params) {
        if (params == null||params.size() <=0){
            return url;
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (!url.contains("?")){
            stringBuffer.append("?");
        }else {
            if (!url.endsWith("?")){
                stringBuffer.append("&");
            }
        }
        for (Map.Entry<String,Object> entry :params.entrySet()){
            stringBuffer.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        stringBuffer.deleteCharAt(stringBuffer.length()-1);
        return url+stringBuffer.toString();
    }
    //解析一个类上面的信息
    public static Class<?> analysisClazzInfo(Object object){
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }


}
