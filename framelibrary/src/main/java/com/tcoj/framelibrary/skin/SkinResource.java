package com.tcoj.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/12/13 0013.
 * 皮肤资源
 */

public class SkinResource {
    private static final String TAG = "SkinResource";
    //资源通过该对象获取
    private Resources mSkinResources;

    private String mPackageName;

    public SkinResource(Context context,String path) {
        Resources sResources = context.getResources();

        try {
            //创建AssetManager
            AssetManager assetManager = AssetManager.class.newInstance();
            //添加下载到本地的皮肤资源
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);

            method.invoke(assetManager,path);
            //创建Resources
            mSkinResources = new Resources(assetManager,sResources.getDisplayMetrics(),sResources.getConfiguration());

            //获取包名
            mPackageName = context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).packageName;
            Log.d(TAG, "SkinResource: "+mPackageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //通过资源名字获取图片
    public Drawable getDrawableByName(String resName){
        try {
            int resId = mSkinResources.getIdentifier(resName,"drawable",mPackageName);
            Drawable drawable = mSkinResources.getDrawable(resId);
            return drawable;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    //通过资源名字获取颜色
    public ColorStateList getColorByName(String resName){
        try {
            int resId = mSkinResources.getIdentifier(resName,"color",mPackageName);
            ColorStateList colorStateList = mSkinResources.getColorStateList(resId);
            return colorStateList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
