package com.example.ciheng.shoppingmap.Adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class urlAdapter {                                                 //This adapter is used to store the urls used in json
    private final String firebaseURL = "gs://shoppingmap-209612";
    private static final String serverURL = "http://api.a17-sd207.studev.groept.be";
    private static final String upload_picture = "http://a17-sd207.studev.groept.be/peng/uploadurl.php?&url=";
    private static final String upload_thumbnail = "http://a17-sd207.studev.groept.be/peng/thumbnail.php?&url=";
    private static final String image_test = "http://api.a17-sd207.studev.groept.be/image_test";
    private static final String mapLocation = "http://api.a17-sd207.studev.groept.be/productLocation";
    //private static final String addressUrl = "http://api.a17-sd207.studev.groept.be/addresscoder/";
    private static final String wishlist_like ="http://api.a17-sd207.studev.groept.be/wishlist_like/";
    private static final String check_like="http://api.a17-sd207.studev.groept.be/check_like/";
    private static final String wishlist_unlike ="http://api.a17-sd207.studev.groept.be/wishlist_unlike/";
    public urlAdapter() {
    }

    public String genAddProductUrl(String productName, String productPrice, String description, int pOwner, String pictureName, String addr) {
        String url = serverURL + "/add_product/" + productName.replaceAll(" ","%20") + "/" + productPrice + "/" + description.replaceAll(" ","%20") + "/" + pOwner + "/" + pictureName+addr;
        return url;
    }

    public String genFindUser(String username_tbc) {
        String url = serverURL + "/findUser/" + username_tbc;
        return url;
    }

    public String genRegister(String email_tba, String pwd_tba, String username_tba, String addr_tba) {
        String url = serverURL + "/signIn/" + email_tba + "/" + pwd_tba + "/" + username_tba + "/" + addr_tba;
        return url;
    }

    public String genAddPicture(String url_tba, String name) {
        String url = upload_picture + url_tba + "&name=" + name;
        return url;
    }

    public String genAddThumbnail(String url_tba, String name) {
        String url = upload_thumbnail + url_tba + "&name=" + name;
        return url;
    }

    public String addressCoder(Context context, String addr) {
        Geocoder coder = new Geocoder(context);
        List<Address> address = null;
        try {
            address = coder.getFromLocationName(addr, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address location = address.get(0);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String message = "address="+addr+" latitude= "+latitude+" longitude= "+longitude;
        Log.v("urlAdapter",message);
        String uploadURL="/"+addr+"/"+latitude+"/"+longitude;
        return uploadURL;
    }

    public String getServerURL() {
        return serverURL;
    }

    public String getFirebaseURL() {
        return firebaseURL;
    }

    public String getImage_test() {
        return image_test;
    }

    public String getMapLocation() {
        return mapLocation;
    }

    public String wishlist_likeURL(int userID, int productID){
        String url=wishlist_like+userID+"/"+productID;
        return url;
    }

    public String wishlist_unlikeURL(int userID,int productID){
        String url=wishlist_unlike+userID+"/"+productID;
        return url;
    }
    public String wishlist_checklikeURL(int userID, int productID){
        String url=check_like+userID+"/"+productID;
        return url;
    }

}
