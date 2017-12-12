package com.tcoj.baselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class ViewByIdUtil {

    public static void inject(Activity activity){
        inject(new ViewFinder(activity),activity);
    }

    public static void inject(View view){
        inject(new ViewFinder(view),view);
    }

    public static void inject(View view,Object object){
        inject(new ViewFinder(view),object);
    }
    //兼容

    /**
     *
     * @param finder  findViewById的辅助类
     * @param object  反射需要执行的类
     */
    public static void inject(ViewFinder finder,Object object){
        injectFields(finder,object);

        injectEvents(finder,object);
    }


    /**
     * 注入属性
     * @param finder
     * @param object
     */
    private static void injectFields(ViewFinder finder, Object object) {
        //1.获取对象中所有的属性
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            //得到属性的ViewById注解
            ViewById viewById = field.getAnnotation(ViewById.class);
            //判断属性是否有注解
            if (viewById != null){
                //2.获取ViewById里面的value值
                int viewId = viewById.value();

                //3.找到View
                View view = finder.findViewById(viewId);
                if (view != null){
                    //4.动态的注入
                    field.setAccessible(true);
                    try {
                        field.set(object,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else {
                    throw new RuntimeException(clazz.getSimpleName()+","+field.getName());
                }
            }
        }
    }


    /**
     *注入事件
     * @param finder
     * @param object
     */
    private static void injectEvents(ViewFinder finder, Object object) {
        //获取类中的所有方法
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        //获取OnClick的里面的value值
        for (Method method : methods) {
            ViewOnClick viewById = method.getAnnotation(ViewOnClick.class);
            if (viewById != null){
                //找到View
                int[] viewIds = viewById.value();
                for (int viewId : viewIds) {
                    View view = finder.findViewById(viewId);
                    boolean isCheckNet = method.getAnnotation(CheckNet.class) != null;
                    if (view != null){
                        //设置点击事件
                        view.setOnClickListener(new DefaultOnClickListener(method,object,isCheckNet));
                    }
                }

            }
        }
    }

    /**
     * 点击事件类
     */
    private static class DefaultOnClickListener implements View.OnClickListener{
        private Method mMethod;
        private Object mObject;
        private boolean mIsCheckNet;
        public DefaultOnClickListener(Method method, Object object,boolean isCheckNet) {
            this.mMethod = method;
            this.mObject = object;
            this.mIsCheckNet = isCheckNet;
        }
        //点击事件最后调用该方法
        @Override
        public void onClick(View v) {
            //是否检测网络
            if (mIsCheckNet){
                if (!networkAvailable(v.getContext())){
                    Toast.makeText(v.getContext(),"亲,您的网络被摧毁了,需要维修",Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            //反射注入
            mMethod.setAccessible(true);
            try {
                //方法有参数
                mMethod.invoke(mObject,v);
            } catch (Exception e) {
                e.printStackTrace();
               /* try {
                    //方法无参数
                    mMethod.invoke(mObject,null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }*/

            }
        }

        /**
         * 检测网络
         * @param context
         * @return
         */
        private static boolean networkAvailable(Context context){
            try{
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()){
                    return true;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
    }
}
