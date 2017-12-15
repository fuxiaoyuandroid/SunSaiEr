package com.tcoj.framelibrary.skin.attr;

import android.view.View;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class SkinAttr {
    //资源名称
    private String mResName;
    //类型
    private SkinType mSkinType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName = resName;
        this.mSkinType = skinType;
    }

    public void skin(View view) {
        mSkinType.skin(view,mResName);
    }
}
