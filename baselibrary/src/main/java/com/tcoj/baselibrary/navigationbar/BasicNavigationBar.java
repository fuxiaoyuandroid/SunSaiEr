package com.tcoj.baselibrary.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcoj.baselibrary.R;

/**
 * Created by Administrator on 2017/11/28 0028.
 * 导航栏基类
 */

public abstract class BasicNavigationBar<P extends BasicNavigationBar.Builder.BasicNavigationParams> implements StandardNavigationBar {

    private P mParams;
    private View mNavigationView;

    public BasicNavigationBar(P params){
        this.mParams = params;
        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }

    public void setText(int viewId,String text){
        TextView tv = findViewById(viewId);
        if (!TextUtils.isEmpty(text)){
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }


    public void setOnClickListener(int viewId, View.OnClickListener listener){
        findViewById(viewId).setOnClickListener(listener);
    }


    private <T extends View> T findViewById(int viewId) {

        return mNavigationView.findViewById(viewId);
    }

    /**
     * 创建绑定
     */
    private void createAndBindView() {

        //创建
        if (mParams.mParent == null){
            //获取Activity的根布局
            ViewGroup activityRoot = ((Activity)(mParams.mContext)).findViewById(android.R.id.content);
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }

        //处理
        if (mParams.mParent == null){
            return;
        }



        mNavigationView = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId(),mParams.mParent,false);

        //添加
        mParams.mParent.addView(mNavigationView,0);
        //绑定
        applyView();
    }

    /**
     * 内部类
     */
    public abstract static class Builder{
        BasicNavigationParams P;
        public Builder(Context context, ViewGroup parent){
            P = new BasicNavigationParams(context,parent);
        }

        public abstract BasicNavigationBar builder();

        /**
         * 配置参数
         */
        public static class BasicNavigationParams{
            public Context mContext;
            public ViewGroup mParent;
            public BasicNavigationParams(Context context,ViewGroup parent){
                this.mContext = context;
                this.mParent = parent;
            }
        }

    }
}
