package com.tcoj.framelibrary.http;

import com.tcoj.baselibrary.utils.MD5Util;
import com.tcoj.framelibrary.db.DaoSupportFactory;
import com.tcoj.framelibrary.db.IDaoSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7 0007.
 *
 */

public class CacheDataUtil {

    public static String getCacheJsonData(String finalUrl){
        final IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        List<CacheData> list = daoSupport.querySupport()
                .selection("mUrlKey=?").selectionArgs(MD5Util.MD5(finalUrl)).query();
        //如果有数据
        if (list.size() != 0) {
            CacheData cacheData = list.get(0);
            String result = cacheData.getResultJson();
            return result;
        }
        return null;
    }


    public static long cacheData(String finalUrl,String result){
        final IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        //先删除旧数据
        daoSupport.delete("mUrlKey=?",MD5Util.MD5(finalUrl));
        //插入新数据
        long number = daoSupport.insert(new CacheData(MD5Util.MD5(finalUrl),result));
        return number;
    }
}
