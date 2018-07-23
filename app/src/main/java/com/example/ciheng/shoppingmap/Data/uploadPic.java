package com.example.ciheng.shoppingmap.Data;

public class uploadPic {
    private String mName;
    private String mImageRefUrl;

    public uploadPic(){}
    public uploadPic(String name, String imageRefUrl){
        if(name.trim().equals("")){
            name="No name";
        }
        mName=name;
        mImageRefUrl = imageRefUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageRefUrl() {
        return mImageRefUrl;
    }

    public void setImageRefUrl(String imageRefUrl) {
        mImageRefUrl = imageRefUrl;
    }
}
