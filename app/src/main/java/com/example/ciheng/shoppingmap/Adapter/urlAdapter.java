package com.example.ciheng.shoppingmap.Adapter;

public class urlAdapter {
    private String mUrl;
    private final String serverURL = "http://api.a17-sd207.studev.groept.be";
    public urlAdapter(){}
    public String genAddProductUrl(String productName,String productPrice,String description,int pOwner){
        mUrl = serverURL + "/add_product/" + productName + "/" + productPrice + "/" + description + "/" + pOwner;
        return mUrl;
    }
}
