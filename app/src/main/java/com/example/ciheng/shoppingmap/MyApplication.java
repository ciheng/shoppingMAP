package com.example.ciheng.shoppingmap;

import android.app.Application;
/**
 * Created by 39112 on 2018/7/15.
 */

public class MyApplication extends Application {
    public static String username; //name用public声明

    @Override

    public void onCreate() {

        super.onCreate();

        setName(NAME); //初始化全局变量

    }

    public String getName() {//调用此函数可以获得name的值.

        return username;

    }

    public void setName(String name) {//调用此函数可以改变name的值，name是一个字符串类型的数据.

        this.username = name;

    }

    private static final String NAME = "MyApplication";//将name初始化为Name.

}