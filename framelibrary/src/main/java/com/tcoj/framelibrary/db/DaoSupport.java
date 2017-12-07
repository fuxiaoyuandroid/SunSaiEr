package com.tcoj.framelibrary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.tcoj.framelibrary.db.curd.QuerySupport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/5 0005.
 */

public class DaoSupport<T> implements IDaoSupport<T> {
    private static final String TAG = "ss";
    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClazz;
    private QuerySupport<T> mQuerySupport;
    //优化
    private static final Object[] mPutMethodArgs = new Object[2];

    private static final Map<String, Method> mPutMethods = new ArrayMap<>();

    @Override
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mSQLiteDatabase = sqLiteDatabase;
        this.mClazz = clazz;
        //创建数据库中的表
        createTable();
    }

    /**
     * 创建表
     */
    private void createTable() {
        String mSQL;
        StringBuffer sb = new StringBuffer();
        //固定的内容
        sb.append("create table if not exists ").append(DaoUtil.getTableName(mClazz))
                .append("(id integer primary key autoincrement, ");
        //通过反射获取字段拼接数据库语句
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            String value = field.getType().getSimpleName();
            sb.append(name).append(DaoUtil.getColumnType(value)).append(",");
        }
        sb.replace(sb.length()-1,sb.length(),")");

        mSQL = sb.toString();
        Log.d(TAG, "createTable: "+mSQL);
        mSQLiteDatabase.execSQL(mSQL);
    }

    //插入任意类型单条数据
    @Override
    public long insert(T data) {
        //通过封装原生的方法
        ContentValues values = contentValuesByData(data);
        return mSQLiteDatabase.insert(DaoUtil.getTableName(mClazz),null,values);
    }

    @Override
    public void insert(List<T> datas) {
        //批量插入使用事务
        mSQLiteDatabase.beginTransaction();
        for (T data : datas) {
            insert(data);
        }
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    @Override
    public QuerySupport<T> querySupport() {
        if (mQuerySupport == null){
            mQuerySupport = new QuerySupport<>(mClazz,mSQLiteDatabase);
        }
        return mQuerySupport;
    }

    /*@Override
    public List<T> query() {
        Cursor cursor = mSQLiteDatabase.query(DaoUtil.getTableName(mClazz),null,null,null,null,null,null);
        return cursorToList(cursor);
    }

    private List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();
        //如果有游标
        if (cursor != null && cursor.moveToFirst()){
            do {
                try {

                    T instance = mClazz.newInstance();
                    Field[] fields = mClazz.getDeclaredFields();
                    for (Field field : fields) {
                        //遍历属性
                        field.setAccessible(true);
                        String name = field.getName();
                        //获取角标
                        int index = cursor.getColumnIndex(name);
                        if (index == -1){
                            continue;
                        }

                        //通过反射获取 游标的方法
                        Method cursorMethod = cursorMethod(field.getType());
                        if (cursorMethod != null){
                            Object value = cursorMethod.invoke(cursor,index);
                            if (value == null){
                                continue;
                            }
                            //处理一些特殊的部分
                            if (field.getType() == boolean.class || field.getType() == Boolean.class){
                                if ("0".equals(String.valueOf(value))){
                                    value = false;
                                }else if ("1".equals(String.valueOf(value))){
                                    value = true;
                                }
                            }else if (field.getType() == char.class || field.getType() == Character.class){
                                value = ((String)value).charAt(0);
                            }else if (field.getType() == Date.class){
                                long date = (Long) value;
                                if (date<=0){
                                    value = null;
                                }else {
                                    value = new Date(date);
                                }
                            }
                            //通过反射注入
                            field.set(instance,value);
                        }
                    }
                    //加入集合
                    list.add(instance);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private Method cursorMethod(Class<?> type) throws Exception{
        String methodName = getColumnMethodName(type);
        Method method = Cursor.class.getMethod(methodName,int.class);
        return method;
    }

    private String getColumnMethodName(Class<?> fieldType) {
        String typeName;
        if (fieldType.isPrimitive()){
            typeName = DaoUtil.capitalize(fieldType.getName());
        }else {
            typeName = fieldType.getSimpleName();
        }
        String methodName = "get"+typeName;
        if ("getBoolean".equals(methodName)){
            methodName = "getInt";
        }else if ("getChar".equals(methodName)||"getCharacter".equals(methodName)){
            methodName = "getString";
        }else if ("getDate".equals(methodName)){
            methodName = "getLong";
        }else if ("getInteger".equals(methodName)){
            methodName = "getInt";
        }
        return methodName;
    }
*/
    @Override
    public int delete(String whereClause, String...whereArgs) {
        return mSQLiteDatabase.delete(DaoUtil.getTableName(mClazz),whereClause,whereArgs);
    }

    @Override
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = contentValuesByData(obj);
        return mSQLiteDatabase.update(DaoUtil.getTableName(mClazz),values,whereClause,whereArgs);
    }


    private ContentValues contentValuesByData(T data) {
        ContentValues values = new ContentValues();

        Field[] fields = mClazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(data);
                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;
                //使用反射获取方法   但是反射在一定程度上会影响性能  参照源码Activity的启动流程  此方法也需要缓存
                String fieldTypeName = field.getType().getName();
                Method putMethod = mPutMethods.get(fieldTypeName);
                //判断缓存中是否有
                if (putMethod == null){
                    putMethod = ContentValues.class.getDeclaredMethod("put",String.class,value.getClass());
                    mPutMethods.put(fieldTypeName,putMethod);
                }
                //通过反射执行
                putMethod.invoke(values,mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }

        }
        return values;
    }
}
