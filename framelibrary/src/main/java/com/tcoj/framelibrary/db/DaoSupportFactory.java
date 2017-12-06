package com.tcoj.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/12/5 0005.
 * 数据库工厂
 */

public class DaoSupportFactory {

    public static DaoSupportFactory mFactory;

    public SQLiteDatabase mSQLiteDatabase;
    //持有外部数据库的引用
    protected DaoSupportFactory() {
        File dbFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+File.separator+"ss"+File.separator+"database");
        if (!dbFile.exists()){
            dbFile.mkdirs();
        }
        File db = new File(dbFile,"ss.db");
        //打开或者创建一个数据库
        mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(db,null);
    }

    //单例
    public static DaoSupportFactory getFactory(){
        if (mFactory == null){
            synchronized (DaoSupportFactory.class){
                if (mFactory == null){
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz){
        IDaoSupport<T> daoSupport = new DaoSupport();
        daoSupport.init(mSQLiteDatabase,clazz);
        return daoSupport;
    }


}
