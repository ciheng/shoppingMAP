package com.example.ciheng.shoppingmap.Data;

/**
 * Created by 39112 on 2018/7/28.
 */

public class message {
    public String senderName;
    public String receiverName;
    public String message;
    public int productID;
    public int senderID;
    public int receiverID;
    private String productUrl;
    private String thumbnailUrl;
    public  int msgID;

    public int getMsgID() {
        return msgID;
    }

    public void setMsgID(int msgID) {
        this.msgID = msgID;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public int getSenderID() {
        return senderID;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

}
