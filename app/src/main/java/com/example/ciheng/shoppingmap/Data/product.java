package com.example.ciheng.shoppingmap.Data;

/**
 * Created by 39112 on 2018/7/24.
 */

public class product {
    private String name;
    private int owner;
    private String price;
    private String descreption;
    private int imageId;

    public product(String name, int owner,String price,String descreption) {
        this.name = name;
        this.owner = owner;
        this.price=price;
        this.descreption=descreption;
        this.imageId=imageId;
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

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public void setImageId(int imageId)
    {
        this.imageId=imageId;
    }

    public int getImageId()
    {
        return imageId;
    }

}
