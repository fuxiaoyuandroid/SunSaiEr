package com.tcoj.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import com.tcoj.baselibrary.R;

/**
 * Created by Administrator on 2017/11/28 0028.
 * 自定义的万能dialog
 */

public class AlertDialog extends Dialog {

    private AlertController mAlert;


    public AlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        mAlert = new AlertController(this,getWindow());
    }

    /**
     * 设置文本
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mAlert.setText(viewId,text);
    }

    public <T extends View> T getView(int viewId) {

        return (T) mAlert.getView(viewId);
    }

    /**
     * 设置点击事件
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mAlert.setOnClickListener(viewId,listener);
    }

    /**
     * 内部类Builder
     */
    public static class Builder{

        private final AlertController.AlertParams P;

        /**
         * 默认主题
         * @param context
         */
        public Builder(Context context){
            this(context, R.style.dialog);
        }

        /**
         * 自定义主题
         * @param context
         * @param themeResId
         */
        public Builder(Context context,int themeResId){
            P = new AlertController.AlertParams(context,themeResId);
        }

        /**
         * 设置布局
         * @param view
         * @return
         */
        public Builder setContentView(View view){
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        public Builder setContentView(int layoutId){
            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;
        }

        /**
         * 设置文本
         * @param viewId
         * @param text
         * @return
         */
        public Builder setText(int viewId,CharSequence text){
            P.mTextArray.put(viewId,text);
            return this;
        }

        /**
         * 设置点击事件
         * @param viewId
         * @param listener
         * @return
         */
        public Builder setOnClickListener(int viewId,View.OnClickListener listener){
            P.mClickArray.put(viewId,listener);
            return this;
        }

        /**
         * 万能参数配置
         * 全屏
         */
        public Builder fullWidth(){
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }
        /**
         * 万能参数配置
         * 从底部弹出
         */
        public Builder fromBottom(boolean isAnimation){
            if (isAnimation){
                P.mAnimations = R.style.dialog_from_bottom_anim;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }
        /**
         * 万能参数配置
         * 设置宽高
         */
        public Builder setWH(int width,int height){
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        /**
         * 万能参数配置
         * 设置默认动画
         */
        public Builder addDefaultAnimation(){
            P.mAnimations = R.style.dialog_anim_scale;
            return this;
        }

        /**
         * 万能参数配置
         * 设置动画
         */
        public Builder addAnimations(int style){
            P.mAnimations = style;
            return this;
        }
        /**
         * 相关listener
         * @param cancelable
         * @return
         */
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }



        public AlertDialog create(){
            final AlertDialog alertDialog = new AlertDialog(P.mContext,P.mThemeResId);
            P.apply(alertDialog.mAlert);
            alertDialog.setCancelable(P.mCancelable);
            if (P.mCancelable){
                alertDialog.setCanceledOnTouchOutside(true);
            }
            alertDialog.setOnCancelListener(P.mOnCancelListener);
            alertDialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null){
                alertDialog.setOnKeyListener(P.mOnKeyListener);
            }
            return alertDialog;
        }

        public AlertDialog show(){
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

}
