package com.tcoj.framelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcoj.framelibrary.skin.SkinManager;
import com.tcoj.framelibrary.skin.SkinResource;

/**
 * Created by Administrator on 2017/12/13 0013.
 * 类型 枚举
 */

public enum SkinType {

    TEXT_COLOR("textColor"){
        @Override
        public void skin(View view, String resName) {
            //获取资源设置
            SkinResource skinResource = getSkinResource();
            ColorStateList color = skinResource.getColorByName(resName);
            if (color == null){
                Log.d("TAG", "skin: ");
                return;
            }
            TextView textView = (TextView) view;
            textView.setTextColor(color);
        }
    },BACKGROUND("background"){
        @Override
        public void skin(View view, String resName) {
            //获取资源设置
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null){
                Log.d("TAG", "skin: drawable");
                ImageView imageView = (ImageView) view;
                imageView.setBackground(drawable);
                return;
            }

            ColorStateList color = skinResource.getColorByName(resName);
            if (color != null){
                Log.d("TAG", "skin: color");
                view.setBackgroundColor(color.getDefaultColor());
            }
        }
    },SRC("src"){
        @Override
        public void skin(View view, String resName) {
            //获取资源设置
            SkinResource skinResource = getSkinResource();

            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null){
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(drawable);
                return;
            }
        }
    };

    //根据名字调对应的方法
    private String mResName;

    SkinType(String resName) {
        this.mResName = resName;
    }


    //抽象方法
    public abstract void skin(View view, String resName);

    public String getResName() {
        return mResName;
    }

    public SkinResource getSkinResource(){
        return SkinManager.getInstance().getSkinResource();
    }
}
