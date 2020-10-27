package com.example.demo;

public class PayParams {
    private String userKey; //必填
    private float price; //必填
    private int type; //必填
    private String outTradeNo; //必填，调用者保证每次不一样，防止和别人重复，建议把公司名作为前缀
    private String outUserNo; //选填
    private String tradeContent; //选填
    private String notifyUrl; //必填
    private String returnUrl; //选填
    private String key; //必填

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutUserNo(String outUserNo) {
        this.outUserNo = outUserNo;
    }

    public String getOutUserNo() {
        return outUserNo;
    }

    public void setTradeContent(String tradeContent) {
        this.tradeContent = tradeContent;
    }

    public String getTradeContent() {
        return tradeContent;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }
}
