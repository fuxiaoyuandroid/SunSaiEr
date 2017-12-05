package com.tcoj.baselibrary.http;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class OkHttpEngine implements HttpEngine{
    private static final String TAG = "OkHttpEngine";

    private static OkHttpClient mOkHttpClient =  new OkHttpClient();

    @Override
    public void get(Context context, String url, HashMap<String, Object> params, final EngineCallBack callBack) {
        url = HttpUtils.jointParams(url,params);

        Log.d(TAG, "get: "+url);

        Request.Builder requestBuilder = new Request.Builder().url(url).tag(context);

        requestBuilder.method("GET",null);

        final Request request = requestBuilder.build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "resultGet: "+result);
                callBack.onSuccess(result);
            }
        });
    }

    @Override
    public void post(Context context, String url, HashMap<String, Object> params, final EngineCallBack callBack) {
        final String jointUrl = HttpUtils.jointParams(url,params);

        Log.d(TAG, "post: "+jointUrl);

        if (callBack != null){
            RequestBody requestBody = appendBody(params);

            Request request = new Request.Builder()
                    .url(url)
                    .tag(context)
                    .post(requestBody)
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callBack.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.d(TAG, "resultPost: "+result);
                    callBack.onSuccess(result);
                }
            });

        }
    }

    /**
     * 拼接参数
     * @param params
     * @return
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        addParams(builder,params);
        return builder.build();
    }

    /**
     * 添加参数
     * @param builder
     * @param params
     */
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()){
            for (String key:params.keySet()){
                builder.addFormDataPart(key,params.get(key)+"");
                Object value = params.get(key);
                if (value instanceof File){
                    //处理文件
                    File file = (File) value;
                    builder.addFormDataPart(key,file.getName(), RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())),file));
                }else if (value instanceof List){
                    //提交的是List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key+i,file.getName(), RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())),file));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    builder.addFormDataPart(key,value+"");
                }
            }
        }
    }

    /**
     * 猜测文件类型
     * @param path
     * @return
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null){
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
