package com.example.ciheng.shoppingmap.Adapter;

public class urlAdapter {
    private final String firebaseURL = "gs://shoppingmap-209612";
    private static final String serverURL = "http://api.a17-sd207.studev.groept.be";
    private static final String upload_picture="http://a17-sd207.studev.groept.be/peng/uploadurl.php?&url=";
    private static final String upload_thumbnail="http://a17-sd207.studev.groept.be/peng/thumbnail.php?&url=";
    private static final String image_test="http://api.a17-sd207.studev.groept.be/image_test";
    private static final String mapLocation ="http://api.a17-sd207.studev.groept.be/productLocation";
    public urlAdapter() {
    }

    public String genAddProductUrl(String productName, String productPrice, String description, int pOwner,String pictureName) {
        String url = serverURL + "/add_product/" + productName + "/" + productPrice + "/" + description + "/" + pOwner+"/"+pictureName;
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
    public String genAddPicture(String url_tba, String name){
        String url=upload_picture+url_tba+"&name="+name;
        return url;
    }
    public String genAddThumbnail(String url_tba, String name){
        String url=upload_thumbnail+url_tba+"&name="+name;
        return url;
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
}
