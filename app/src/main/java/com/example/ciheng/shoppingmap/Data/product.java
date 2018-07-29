package com.example.ciheng.shoppingmap.Data;

/**
 * Created by 39112 on 2018/7/24.
 */

public class product {
    private String name;
    private int owner;
    private String price;
    private String description;
    private int productId;
    private String downloadUrl;
    private String thumbnailUrl;

    public product(String name, int owner,String price,String description,int productId) {
        this.name = name;
        this.owner = owner;
        this.price=price;
        this.description =description;
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public int getProductId()
    {
        return productId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl.replaceAll("\\/","/");
    }
    public void setThumbnailUrl(String thumbnailUrl){
        this.thumbnailUrl=downloadUrl.replaceAll("\\/","/");
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
