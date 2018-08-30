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
    public int ownerId;
    public String productUrl;
    public int msgID;
    public boolean isOwnerReceiver;

    public int getMsgID() {
        return msgID;
    }

    public void setMsgID(int msgID) {
        this.msgID = msgID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl.replaceAll("\\/", "/");
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



    public String getSenderName() {
        return senderName;
    }

    public void setMessage(String message) {
        this.message = message.replaceAll("%20", " ");
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }



    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean getIsOwnerReceiver() {
        return isOwnerReceiver;
    }

    public void setOwnerReceiver(boolean ownerReceiver) {
        isOwnerReceiver = ownerReceiver;
    }
}
