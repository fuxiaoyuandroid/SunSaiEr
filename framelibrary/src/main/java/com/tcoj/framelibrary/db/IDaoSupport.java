package com.tcoj.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import com.tcoj.framelibrary.db.curd.QuerySupport;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5 0005.
 * 数据库规范 增删改查
 */

public interface IDaoSupport<T> {
    //数据库
    void init(SQLiteDatabase sqLiteDatabase,Class<T> clazz);

    //插入单条数据
    long insert(T data);
    //插入多条数据
    void insert(List<T> datas);

    //查询
    /*List<T> query();*/
    QuerySupport<T> querySupport();
    //删除
    int delete(String whereClause,String...whereArgs);

    //修改
    int update(T obj,String whereClause,String...whereArgs);
}
