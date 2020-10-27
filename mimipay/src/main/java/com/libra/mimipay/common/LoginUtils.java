package com.libra.mimipay.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.libra.mimipay.MimiPayApplication;
import com.libra.mimipay.common.HttpUtils.HttpCallback;
import java.text.DecimalFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginUtils {

    public interface LoginCallback {
        void onLoginResult(String str);
    }

    public interface LogoutCallback {
        void onLogoutResult(boolean z);
    }

    public static void login(Context context, LoginCallback cb) {
        SharedPreferences pref = context.getSharedPreferences("data", 0);
        login(context, pref.getString("username", ""), pref.getString("password", ""), cb);
    }

    public static void login(final Context context, final String username, final String password, final LoginCallback cb) {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("password", password);
                jsonObject.put("mode", String.format("%s-%s-%s", new Object[]{Build.BRAND, Build.MODEL, VERSION.RELEASE}));
                jsonObject.put("uuid", DeviceUUID.getUUID(context));
                HttpUtils.post(context, "/mobile/login", jsonObject, new HttpCallback() {
                    @Override
                    public void onFailure(int errorCode, Object data) {
                        if (cb != null) {
                            cb.onLoginResult("");
                        }
                    }

                    @Override
                    public void onSuccess(Object data) {
                        Editor editor = context.getSharedPreferences("data", 0).edit();
                        JSONObject json = (JSONObject) data;
                        MimiPayApplication application = (MimiPayApplication) ((Activity) context).getApplication();
                        String token = null;
                        try {
                            token = json.getString("token");
                            application.setToken(token);
                            application.setUsername(json.getString("username"));
                            application.setBalance(new DecimalFormat("0.00").format(json.getDouble("balance")));
                            application.setDeviceName(json.getString("deviceName"));
                            application.setWxNotifyPattern(new JSONArray(json.getString("wxNotifyPattern")));
                            application.setAlipayNotifyPattern(new JSONArray(json.getString("alipayNotifyPattern")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.commit();
                        if (cb != null) {
                            cb.onLoginResult(token);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void logout(final Context context, String token, final LogoutCallback cb) {
        if (!TextUtils.isEmpty(token)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", token);
                jsonObject.put("uuid", DeviceUUID.getUUID(context));
                HttpUtils.post(context, "/mobile/logout", jsonObject, new HttpCallback() {
                    @Override
                    public void onFailure(int errorCode, Object data) {
                        if (cb != null) {
                            cb.onLogoutResult(false);
                        }
                    }

                    @Override
                    public void onSuccess(Object data) {
                        Editor editor = context.getSharedPreferences("data", 0).edit();
                        editor.remove("username");
                        editor.remove("password");
                        editor.commit();
                        if (cb != null) {
                            cb.onLogoutResult(true);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
