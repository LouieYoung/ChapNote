package com.example.chapnote;

import android.app.Application;
import android.content.Context;

import jackmego.com.jieba_android.JiebaSegmenter;

public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        //异步初始化
        JiebaSegmenter.init(getApplicationContext());
        //获取context
        mContext = getApplicationContext();
    }
    //创建一个静态的方法，以便获取context对象
    public static Context getContext(){
        return mContext;
    }
}
