package com.tcoj.framelibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tcoj.baselibrary.navigationbar.BasicNavigationBar;

/**
 * Created by Administrator on 2017/11/28 0028.
 * 基本NavigationBar
 */

public class DefaultNavigationBar extends BasicNavigationBar<DefaultNavigationBar.DefaultBuilder.DefaultNavigationParams> {

    public DefaultNavigationBar(DefaultNavigationBar.DefaultBuilder.DefaultNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        //设置文本
        setText(R.id.title_tv,getParams().mTitle);
        setText(R.id.right_tv,getParams().mRightText);
      /*  setImageResource(R.id.right_iv, getParams().mResId);*/
        //设置点击事件
        setOnClickListener(R.id.right_tv,getParams().mRightClickListener);
        //左边的点击事件都是默认的
        setOnClickListener(R.id.left_tv,getParams().mLeftClickListener);

    }

    public static class DefaultBuilder extends BasicNavigationBar.Builder{
        DefaultNavigationParams P;
        public DefaultBuilder(Context context) {
            super(context, null);
            P = new DefaultNavigationParams(context,null);
        }
        public DefaultBuilder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context,parent);
        }

        @Override
        public DefaultNavigationBar builder() {
            DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar(P);
            return defaultNavigationBar;
        }
        //设置效果

        public DefaultBuilder setTitle(String title){
            P.mTitle = title;
            return this;
        }


        public DefaultBuilder setRightText(String rightText){
            P.mRightText = rightText;
            return this;
        }

        public DefaultBuilder setRightIcon(int resId){
            P.mResId = resId;
            return this;
        }

        public DefaultBuilder setRightClickListener(View.OnClickListener rightListener){
            P.mRightClickListener = rightListener;
            return this;
        }

        public DefaultBuilder setLeftClickListener(View.OnClickListener leftListener){
            P.mLeftClickListener = leftListener;
            return this;
        }

        public static class DefaultNavigationParams extends BasicNavigationParams{
            //标题
            public String mTitle;
            //右侧内容
            public String mRightText;
            //右侧图片
            public int mResId;

            //右边的点击事件
            public View.OnClickListener mRightClickListener;
            //默认左边的点击事件
            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //关闭当前activity
                    ((Activity)mContext).finish();
                }
            };
            //实现效果
            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
