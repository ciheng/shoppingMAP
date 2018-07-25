package com.example.ciheng.shoppingmap.Data;

public class uploadPic {
    private String mName;
    private String mImageUrl;
    private String mThumbnailUrl;
    private String mProductId;
    public uploadPic(String name, String imageUrl){
        if(name.trim().equals("")){
            name="No name";
        }
        mName=name;
        mImageUrl = imageUrl;
    }
    public uploadPic(String name){
        if(name.trim().equals("")){
            name="No name";
        }
        mName=name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
        }

    public String getProductId() {
        return mProductId;
    }

    public void setProductId(String productId) {
        mProductId = productId;
    }


}
