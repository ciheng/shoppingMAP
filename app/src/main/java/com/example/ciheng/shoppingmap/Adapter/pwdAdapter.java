package com.example.ciheng.shoppingmap.Adapter;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class pwdAdapter {
    private static final String TAG = "pwdAdapter";
    public String md5(String text) {                   //security type md5
        MessageDigest digest = null;
        Log.v(TAG,text);
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result){
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1){
                    sb.append("0"+hex);
                }else {
                    sb.append(hex);
                }
            }
            Log.v(TAG,sb.toString());
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
