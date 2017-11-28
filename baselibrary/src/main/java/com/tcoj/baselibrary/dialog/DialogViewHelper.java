package com.tcoj.baselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/11/28 0028.
 */

class DialogViewHelper {
    private View mContentView = null;

    private SparseArray<WeakReference<View>> mViews;

    public DialogViewHelper(Context context, int layoutResId) {
        this();
        mContentView = LayoutInflater.from(context).inflate(layoutResId,null);
    }

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    /**
     * 设置布局
     * @param view
     */
    public void setContentView(View view) {
        this.mContentView = view;
    }

    /**
     * 设置文本
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null){
            tv.setText(text);
        }
    }

    public <T extends View> T getView(int viewId) {
        //软引用 防止侧漏
        WeakReference<View> viewReference = mViews.get(viewId);
        View view = null;
        if (viewReference != null){
            view = viewReference.get();
        }
        if (view == null){
            view = mContentView.findViewById(viewId);
            if (view != null){
                mViews.put(viewId,new WeakReference<>(view));
            }
        }
        return (T) view;
    }

    /**
     * 设置点击事件
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null){
            view.setOnClickListener(listener);
        }
    }

    /**
     * 获取
     * @return
     */
    public View getContentView() {
        return mContentView;
    }
}
