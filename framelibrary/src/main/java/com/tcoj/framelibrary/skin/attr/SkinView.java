package com.tcoj.framelibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 *
 */

public class SkinView {
    //目标
    private View mView;
    //目标的多个属性
    private List<SkinAttr> mSkinAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mSkinAttrs = skinAttrs;
    }

    //遍历目标的属性
    public void skin(){
        for (SkinAttr skinAttr : mSkinAttrs) {
            skinAttr.skin(mView);
        }
    }
}
