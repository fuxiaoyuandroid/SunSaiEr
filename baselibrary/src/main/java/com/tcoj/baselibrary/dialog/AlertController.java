package com.tcoj.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/11/28 0028.
 */

class AlertController {

    private AlertDialog mDialog;
    private Window mWindow;
    private DialogViewHelper mViewHelper;
    public AlertController(AlertDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        this.mViewHelper = viewHelper;
    }

    /**
     * 设置文本
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId,text);
    }

    public <T extends View> T getView(int viewId) {

        return (T) mViewHelper.getView(viewId);
    }

    /**
     * 设置点击事件
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId,listener);
    }
    /**
     * 获取dialog
     * @return
     */
    public AlertDialog getDialog() {
        return mDialog;
    }

    /**
     * 获取window
     * @return
     */
    public Window getWindow() {
        return mWindow;
    }

    /**
     * 内部类AlertParams
     */
    public static class AlertParams{
        //参数
        public Context mContext;
        public int mThemeResId;
        /**
         * 点击空白是否能够取消
         */
        public boolean mCancelable = true;
        /**
         *dialog  cancel监听
         */
        public DialogInterface.OnCancelListener mOnCancelListener;
        /**
         *dialog dismiss监听
         */
        public DialogInterface.OnDismissListener mOnDismissListener;
        /**
         *dialog key监听
         */
        public DialogInterface.OnKeyListener mOnKeyListener;
        /**
         * 控件
         */
        public View mView;
        /**
         * 布局
         */
        public int mViewLayoutResId;
        /**
         * 存放字体
         */
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        /**
         * 存放点击事件
         */
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        //宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        //位置
        public int mGravity = Gravity.CENTER;
        //动画
        public int mAnimations = 0;
        //高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;


        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 设置参数
         * @param alert
         */
        public void apply(AlertController alert) {
            DialogViewHelper viewHelper = null;
            //设置布局
            if (mViewLayoutResId != 0){
                viewHelper = new DialogViewHelper(mContext,mViewLayoutResId);
            }

            if (mView != null){
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }

            if (viewHelper == null){
                throw new IllegalArgumentException("请设置布局setContentView");
            }

            //给Dialog设置布局
            alert.getDialog().setContentView(viewHelper.getContentView());
            //设置ViewHelper的辅助类
            alert.setViewHelper(viewHelper);
            //设置文本 点击
            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                alert.setText(mTextArray.keyAt(i),mTextArray.valueAt(i));
            }

            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                alert.setOnClickListener(mClickArray.keyAt(i),mClickArray.valueAt(i));
            }


            //自定义效果
            Window window = alert.getWindow();
            window.setGravity(mGravity);
            if (mAnimations != 0){
                window.setWindowAnimations(mAnimations);
            }

            WindowManager.LayoutParams params = window.getAttributes();

            params.width = mWidth;
            params.height = mHeight;

            window.setAttributes(params);
        }
    }

}
