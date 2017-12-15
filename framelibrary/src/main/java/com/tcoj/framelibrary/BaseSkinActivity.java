package com.tcoj.framelibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;

import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.tcoj.baselibrary.base.BaseActivity;
import com.tcoj.framelibrary.skin.SkinManager;
import com.tcoj.framelibrary.skin.attr.SkinAttr;
import com.tcoj.framelibrary.skin.attr.SkinView;
import com.tcoj.framelibrary.skin.support.SkinAppCompatViewInflater;
import com.tcoj.framelibrary.skin.support.SkinAttrSupport;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/22 0022.
 * 插件换肤
 */

public abstract class BaseSkinActivity extends BaseActivity {
    private static final String TAG = "BaseSkinActivity";
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;
    private SkinAppCompatViewInflater mAppCompatViewInflater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //拦截View的创建
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        LayoutInflaterCompat.setFactory2(layoutInflater,this);

        super.onCreate(savedInstanceState);
    }
    //创建View  解析属性  统一管理SkinManager
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //创建View
        View view = createView(parent, name, context, attrs);
        Log.d(TAG, "onCreateView:"+view);

        //一个Activity的布局对应多个SkinAttr(List集合)
        if (view != null) {

            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);

            SkinView skinView = new SkinView(view,skinAttrs);

            managerSkinView(skinView);
        }
        return view;
    }

    private void managerSkinView(SkinView skinView) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if (skinViews == null){
            skinViews = new ArrayList<>();
            SkinManager.getInstance().register(this,skinViews);
        }
        skinViews.add(skinView);
    }

    //拷贝系统的源码
    @SuppressLint("RestrictedApi")
    private View createView(View parent, String name, Context context, AttributeSet attrs) {
        if (mAppCompatViewInflater == null) {

            mAppCompatViewInflater = new SkinAppCompatViewInflater();

        }

        boolean inheritContext = false;

        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }
}
