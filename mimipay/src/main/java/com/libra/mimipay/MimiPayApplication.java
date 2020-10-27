package com.libra.mimipay;

import android.app.Application;
import org.json.JSONArray;

public class MimiPayApplication extends Application {
    private JSONArray alipayNotifyPattern;
    private String balance;
    private boolean dev = false;
    private String deviceName;
    private String token;
    private String userKey;
    private String username;
    private JSONArray wxNotifyPattern;

    public void clear() {
        this.userKey = "";
        this.token = "";
        this.username = "";
        this.balance = "";
        this.deviceName = "";
        this.dev = false;
    }

    public void setDev(boolean dev2) {
        this.dev = dev2;
    }

    public boolean getDev() {
        return this.dev;
    }

    public void setDeviceName(String deviceName2) {
        this.deviceName = deviceName2;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setUsername(String username2) {
        this.username = username2;
    }

    public String getUsername() {
        return this.username;
    }

    public void setBalance(String balance2) {
        this.balance = balance2;
    }

    public String getBalance() {
        return this.balance;
    }

    public void setToken(String token2) {
        this.token = token2;
    }

    public String getToken() {
        return this.token;
    }

    public void setUserKey(String userKey2) {
        this.userKey = userKey2;
    }

    public String getUserKey() {
        return this.userKey;
    }

    public void setWxNotifyPattern(JSONArray wxNotifyPattern2) {
        this.wxNotifyPattern = wxNotifyPattern2;
    }

    public JSONArray getWxNotifyPattern() {
        return this.wxNotifyPattern;
    }

    public void setAlipayNotifyPattern(JSONArray alipayNotifyPattern2) {
        this.alipayNotifyPattern = alipayNotifyPattern2;
    }

    public JSONArray getAlipayNotifyPattern() {
        return this.alipayNotifyPattern;
    }
}
