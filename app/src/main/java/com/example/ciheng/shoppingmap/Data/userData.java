package com.example.ciheng.shoppingmap.Data;

import android.app.Application;
/**
 * Created by 39112 on 2018/7/15.
 */

public class userData extends Application {
    private String mUserName;
    private int mUserId;

    @Override

    public void onCreate() {

        super.onCreate();
       // setUserName(NAME);

    }

    public String getUserName() {
        return mUserName;

    }

    public void setUserName(String name) {
        this.mUserName = name;

    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        this.mUserId = userId;
    }
    //private static final String NAME = "userData";
}