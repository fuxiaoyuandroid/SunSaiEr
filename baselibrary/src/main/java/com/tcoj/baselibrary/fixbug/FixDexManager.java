package com.tcoj.baselibrary.fixbug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class FixDexManager {

    private Context mContext;

    private File mDexDir;
    public FixDexManager(Context context) {
        this.mContext = context;
        this.mDexDir = context.getDir("odex",Context.MODE_PRIVATE);
    }

    /**
     * 修复
     * @param fixDexPath
     */
    public void fixDex(String fixDexPath) throws Exception{

        //2.下载修复后的DexElement

        //2.1移动到系统能够访问的dex目录下
        File srcFile = new File(fixDexPath);
        if (!srcFile.exists()){
            throw new FileNotFoundException(fixDexPath);
        }
        File destFile = new File(mDexDir,srcFile.getName());

        if (destFile.exists()){
            Log.d("dex", "fixDex: loaded");
            return;
        }

        copyFile(srcFile,destFile);

        //2.2ClassLoader读取dex路径
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);


        fix(fixDexFiles);
    }

    /**
     * 注入
     * @param classLoader
     * @param dex
     * @throws Exception
     */
    private void injectDexElements(ClassLoader classLoader, Object dex) throws Exception{
        Log.d("dex", "fixDex: 注入了");
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        //获取pathList
        Field pathListFiled = classLoaderClass.getDeclaredField("pathList");
        pathListFiled.setAccessible(true);
        Object pathList = pathListFiled.get(classLoader);

        Field dexElementsFiled = pathList.getClass().getDeclaredField("dexElements");
        dexElementsFiled.setAccessible(true);
        dexElementsFiled.set(pathList,dex);
    }

    /**
     * 通过ClassLoader获取dexElements
     * @param loader
     * @return
     */
    private Object getDexElementsByCl(ClassLoader loader) throws Exception{
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        //获取pathList
        Field pathListFiled = classLoaderClass.getDeclaredField("pathList");
        pathListFiled.setAccessible(true);
        Object pathList = pathListFiled.get(loader);

        Field dexElementsFiled = pathList.getClass().getDeclaredField("dexElements");
        dexElementsFiled.setAccessible(true);
        return dexElementsFiled.get(pathList);
    }

    //拷贝
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 合并两个dexElements数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    /**
     * 加载方法
     * @throws Exception
     */
    public void loadFixDex() throws Exception{
        File[] dexFiles = mDexDir.listFiles();
        List<File> fixDexFiles = new ArrayList<>();
        for (File dexFile : dexFiles) {
            if (dexFile.getName().endsWith(".dex")){
                fixDexFiles.add(dexFile);
            }
        }
        fix(fixDexFiles);
    }

    /**
     * 修复
     * @param fixDexFiles
     * @throws Exception
     */
    private void fix(List<File> fixDexFiles) throws Exception {
        //1.先获取已经运行的DexElement
        ClassLoader classLoader = mContext.getClassLoader();
        Object dexElements = getDexElementsByCl(classLoader);

        File od = new File(mDexDir,"odex");
        if (!od.exists()){
            od.mkdirs();
        }
        //修复
        for (File fixDexFile : fixDexFiles) {
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(fixDexFile.getAbsolutePath(),od,null,classLoader);
            Object fixDexElements = getDexElementsByCl(fixDexClassLoader);
            //3.把补丁的DexElement插到已经运行的最前面 合并
            dexElements = combineArray(fixDexElements,dexElements);
            Log.d("dex", "fixDex: 合并了吗");
        }

        //注入到原来的类中
        injectDexElements(classLoader,dexElements);
    }

}
