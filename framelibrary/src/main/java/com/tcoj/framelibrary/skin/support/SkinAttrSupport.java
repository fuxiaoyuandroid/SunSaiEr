package com.tcoj.framelibrary.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.tcoj.framelibrary.skin.attr.SkinAttr;
import com.tcoj.framelibrary.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 * 皮肤属性解析的支持类
 */

public class SkinAttrSupport {
    private static final String TAG = "SkinAttrSupport";
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        //background  textColor  src
        List<SkinAttr> skinAttrs = new ArrayList<>();

        int attrLength = attrs.getAttributeCount();

        for (int i = 0; i < attrLength; i++) {
            //获取名称 attrName textSize, attrValue  20.0sp
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            Log.d(TAG, "getSkinAttrs: attrName "+attrName+", attrValue  "+attrValue);

            //获取必要的
            SkinType skinType = getSkinType(attrName);
            if (skinType != null){
                //资源名称和类型  目前只有attrValue 是一个@int类型的数据
                String resName = getResName(context,attrValue);

                if (TextUtils.isEmpty(resName)){
                    continue;
                }
                SkinAttr skinAttr = new SkinAttr(resName,skinType);

                skinAttrs.add(skinAttr);
            }
        }
        return skinAttrs;
    }

    /**
     * 获取资源的名称
     * @param context
     * @param attrValue
     * @return
     */
    private static String getResName(Context context, String attrValue) {
        //判断   background可能是#或@
        if (attrValue.startsWith("@")){
            //截取
            attrValue = attrValue.substring(1);
            //转成int
            int resId = Integer.parseInt(attrValue);
            //得到资源名
            String resName = context.getResources().getResourceEntryName(resId);
            //返回
            return resName;
        }
        return null;
    }

    /**
     * 通过名称获取Type
     * @param attrName
     * @return
     */
    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            if (skinType.getResName().equals(attrName)){
                return skinType;
            }
        }
        return null;
    }
}
