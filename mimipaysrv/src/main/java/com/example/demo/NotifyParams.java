package com.example.demo;

public class NotifyParams {
    private String outTradeNo;
    private String outUserNo;
    private float price;
    private float realPrice;
    private String key;

    public NotifyParams() {
    }

    public NotifyParams(String outTradeNo, String outUserNo, float price, float realPrice) {
        this.outTradeNo = outTradeNo;
        this.outUserNo = outUserNo;
        this.price = price;
        this.realPrice = realPrice;
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

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setRealPrice(float realPrice) {
        this.realPrice = realPrice;
    }

    public float getRealPrice() {
        return realPrice;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
