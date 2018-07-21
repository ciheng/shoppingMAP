package com.example.ciheng.shoppingmap;

import android.app.Application;
/**
 * Created by 39112 on 2018/7/15.
 */

public class MyApplication extends Application {
    public static String username;

    @Override

    public void onCreate() {

        super.onCreate();
        setName(NAME);

    }

    public String getName() {
        return username;

    }

    public void setName(String name) {
        this.username = name;

    }

    private static final String NAME = "MyApplication";
}