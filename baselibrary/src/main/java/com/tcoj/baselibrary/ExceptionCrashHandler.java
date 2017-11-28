package com.tcoj.baselibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/22 0022.
 *
 * 全局异常捕捉类
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionCrashHandler mHandler;
    private Context mContext;
    private static Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    /**
     * 单例
     * @return
     */
    public static ExceptionCrashHandler getInstance(){
        if (mHandler == null){
            //解决多并发问题
            synchronized (ExceptionCrashHandler.class){
                if (mHandler == null){
                    mHandler = new ExceptionCrashHandler();
                }
            }
        }
        return mHandler;
    }

    public void init(Context context){
        this.mContext = context;
        //设置全局的异常类
        Thread.currentThread().setUncaughtExceptionHandler(this);
        defaultUncaughtExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //全局异常
        Log.e("ss", "uncaughtException: 报异常了");
        //写入本地文件 1.异常信息  2.当前App的版本信息  3.手机信息  4.保存当前文件  等应用再次启动上传
        String crashFileName = saveInfoToSD(e);

        cacheCrashFile(crashFileName);

        //系统默认处理
        defaultUncaughtExceptionHandler.uncaughtException(t,e);

    }

    /**
     * 缓存崩溃日志文件
     * @param crashFileName
     */
    private void cacheCrashFile(String crashFileName) {
        SharedPreferences sp = mContext.getSharedPreferences("crash",Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME",crashFileName).commit();
    }
    /**
     * 获取崩溃文件名称
     */
    public File getCrashFile(){
        String crashFileName = mContext.getSharedPreferences("crash",Context.MODE_PRIVATE).getString("CRASH_FILE_NAME","NULL");
        return new File(crashFileName);
    }
    /**
     * 保存获取的信息
     * @param exception
     * @return
     */
    private String saveInfoToSD(Throwable exception) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();
        //第一部分信息   获取APK信息和手机信息
        for (Map.Entry<String,String> entry:obtainSimpleInfo(mContext).entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" - ").append(value).append("\n");
        }
        //第二部分信息   获取异常信息
        sb.append(obtainExceptionInfo(exception));

        //保存文件
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //手机应用目录
            File dir = new File(mContext.getFilesDir()+File.separator+"crash"+File.separator);
            //先删除之前的异常信息
            if (dir.exists()){
                deleteDir(dir);
            }
            //重新创建文件夹
            if (!dir.exists()){
                dir.mkdir();
            }
            //写入文件
            try{
                fileName = dir.toString()+File.separator+getAssignTime("yyyy-MM-dd")+".txt";
                Log.e("ss", "saveInfoToSD: "+fileName);
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return fileName;
    }

    /**
     * 获取简单信息  APK
     * @param context
     * @return
     */
    private HashMap<String,String> obtainSimpleInfo(Context context){
        HashMap<String,String> map = new HashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(),PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName",mPackageInfo.versionName);
        map.put("versionCode",mPackageInfo.versionCode+"");
        map.put("MODEL", Build.MODEL+"");
        map.put("SDK_INT",Build.VERSION.SDK_INT+"");
        map.put("PRODUCT",Build.PRODUCT+"");
        map.put("MOBILE_INFO",getMobileInfo());

        return map;
    }

    /**
     * 获取手机信息
     * @return
     */
    public static String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            //利用反射获取类的属性
            Field[] fields =Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name+" - "+value).append("\n");

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 时间
     * @param time
     * @return
     */
    private String getAssignTime(String time) {
        DateFormat dateFormat = new SimpleDateFormat(time);
        long currentTime = System.currentTimeMillis();
        return dateFormat.format(new Date(currentTime));
    }

    /**
     * 删除
     * @param dir
     */
    private void deleteDir(File dir) {
        if (dir.isDirectory()){
            File[] files = dir.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    /**
     *
     * @param e
     * @return
     */
    private String obtainExceptionInfo(Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        printWriter.close();
        return writer.toString();
    }

}
