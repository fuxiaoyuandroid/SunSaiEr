package com.tcoj.framelibrary.http;

/**
 * Created by Administrator on 2017/12/7 0007.
 */

public class CacheData {
    private String mUrlKey;
    private String mResultJson;

    public CacheData() {
    }

    public CacheData(String mUrlKey, String mResultJson) {
        this.mUrlKey = mUrlKey;
        this.mResultJson = mResultJson;
    }

    public String getUrlKey() {
        return mUrlKey;
    }

    public void setUrlKey(String mUrlKey) {
        this.mUrlKey = mUrlKey;
    }

    public String getResultJson() {
        return mResultJson;
    }

    public void setResultJson(String mResultJson) {
        this.mResultJson = mResultJson;
    }
}
