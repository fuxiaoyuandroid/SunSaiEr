package com.tcoj.framelibrary.skin;

import android.app.Activity;
import android.content.Context;

import com.tcoj.framelibrary.BaseSkinActivity;
import com.tcoj.framelibrary.skin.attr.SkinView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/13 0013.
 * 皮肤管理类
 */

public class SkinManager {
    private static SkinManager mInstance;

    private Context mContext;

    private Map<Activity,List<SkinView>> mSkinViews = new HashMap<>();

    private SkinResource mSkinResource;
    static {
        mInstance = new SkinManager();
    }
    //创建对象
    public static SkinManager getInstance() {

        return mInstance;
    }

    public void init(Context context){
        this.mContext = context.getApplicationContext();
    }

    /**
     * 加载新皮肤
     * @param path
     * @return
     */
    public int loadSkin(String path) {
        //校验签名  增量更新
        //初始化资源管理
        mSkinResource = new SkinResource(mContext,path);
        //改变皮肤
        Set<Activity> activities = mSkinViews.keySet();
        for (Activity activity : activities) {
            List<SkinView> skinViews =  mSkinViews.get(activity);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
        return 0;
    }

    /**
     * 恢复默认
     * @return
     */
    public int restoreDefaultSkin() {
        return 0;
    }

    /**
     * 通过Activity获取SkinView
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    /**
     * 注册
     * @param activity
     * @param skinViews
     */
    public void register(BaseSkinActivity activity, List<SkinView> skinViews) {
        mSkinViews.put(activity,skinViews);
    }

    /**
     * 获取当前的皮肤资源管理
     * @return
     */
    public SkinResource getSkinResource(){
        return mSkinResource;
    }
}
