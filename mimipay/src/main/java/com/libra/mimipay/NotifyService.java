package com.libra.mimipay;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import androidx.core.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.libra.mimipay.common.DeviceUUID;
import com.libra.mimipay.common.HttpUtils;
import com.libra.mimipay.common.HttpUtils.HttpCallback;
import com.libra.mimipay.common.LoginUtils;
import com.libra.mimipay.common.LoginUtils.LoginCallback;
import com.libra.mimipay.common.OnlineLogUtils;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotifyService extends NotificationListenerService {
    public static final String CLASS_NAME_WX = "com.tencent.mm.ui.LauncherUI";
    public static final String PACKAGE_NAME_ALIPAY = "com.eg.android.AlipayGphone";
    public static final String PACKAGE_NAME_WX = "com.tencent.mm";
    private static final int PAYTYPE_ALIPAY = 2;
    private static final int PAYTYPE_NONE = 0;
    private static final int PAYTYPE_WX = 1;
    private static final Logger logger = Logger.getLogger(NotifyService.class.getName());

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x010b, code lost:
        if (r23.contains("元") != false) goto L_0x010d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Override
    public void onNotificationPosted(android.service.notification.StatusBarNotification r29) {

        super.onNotificationPosted(r29);
            Context r28 = this;
            android.app.Notification r16 = r29.getNotification();
            com.libra.mimipay.MimiPayApplication r11 = (com.libra.mimipay.MimiPayApplication) r28.getApplicationContext();
            java.lang.String r4 = r11.getToken();
            JSONArray r25 = r11.getWxNotifyPattern();
            JSONArray r2 = r11.getAlipayNotifyPattern();

            JSONArray r19 = r25;
            String r18 = r29.getPackageName();
            Bundle r13 = r16.extras;
            String r24 = r13.getString("android.title", "");
            String r23 = r13.getString("android.text", "");

            int r5 = 0;
            if(r18.equals("com.tencent.mm")){
                r5 = 1;
            }
            if(r18.equals("com.eg.android.AlipayGphone")){
                r5 = 2;
            }
            else{
                java.lang.String r32 = "模式为null-%d";
                OnlineLogUtils.log(String.format(Locale.ENGLISH, r32, r5));
                return;
            }


            java.lang.String r30 = "%s-%s-%s-pattern len:%d";
            OnlineLogUtils.log(String.format(Locale.ENGLISH, r30, r18, r24, r23, r25.length()));

            if(!r23.contains("元")) {
                java.lang.String r61 = "不能识别的通知 %s-%s-%s";
                OnlineLogUtils.logAndUpload(r28, String.format(Locale.ENGLISH, r61, r18, r24, r23));
            }

            java.lang.String r33 = "模式为0-%d";
            OnlineLogUtils.log(String.format(Locale.ENGLISH, r33, r5));


            java.lang.String r34 = "获取支付类型和价格错误%d-%f";
            java.lang.String r36 = "extra is null";
            java.lang.String r37 = "token:%s version:%d";

            long r8 = r16.when;
            float r6 = (float) 0.0;

            try {
                for(int i = 0; i < r19.length(); i++){
                        JSONObject jsonObject = r19.getJSONObject(i);
                        if(jsonObject.getString("title").equals(r24)){
                            r6 = getPrice(r23, jsonObject.getString("content"));
                        }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            notifyImpl(r4, r5, r6, r8, 0);
    }

    /* access modifiers changed from: private */
    public void notifyImpl(String token, int payType, double price, long time, int doCount) {
        JSONObject jsonObject = new JSONObject();
        try {
            final String uuid = DeviceUUID.getUUID(getApplicationContext());
            jsonObject.put("token", token);
            jsonObject.put("uuid", uuid);
            jsonObject.put("payType", payType);
            jsonObject.put("realPrice", price);
            jsonObject.put("time", time);
            final String str = token;
            final int i = payType;
            final double d = price;
            final long j = time;
            final int i2 = doCount;
            HttpUtils.post(getApplicationContext(), "/mobile/notify", jsonObject, new HttpCallback() {
                @Override
                public void onFailure(int errorCode, Object data) {
                    OnlineLogUtils.log(String.format("通知，post失败 obj：%s", new Object[]{String.format("token:%s-uuid:%s-payType:%d-price:%f-time:%d", new Object[]{str, uuid, Integer.valueOf(i), Double.valueOf(d), Long.valueOf(j)})}));
                    Toast.makeText(NotifyService.this.getApplicationContext(), "notify失败" + errorCode + "  data:" + data, 1).show();
                    if (errorCode == -12) {
                        LoginUtils.login(NotifyService.this, new LoginCallback() {
                            @Override
                            public void onLoginResult(String str) {
                                if (!TextUtils.isEmpty(str) && i2 < 1) {
                                    NotifyService.this.notifyImpl(str, i, d, j, i2 + 1);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onSuccess(Object data) {
                    Toast.makeText(NotifyService.this.getApplicationContext(), "notify成功", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            OnlineLogUtils.log(String.format("通知时json处理失败", new Object[0]));
            e.printStackTrace();
        }
    }

    public static float getPrice(String text, String prefix) {
        float ret = 0.0f;
        int index = text.indexOf(prefix);
        while (index > -1) {
            int start = index + prefix.length();
            try {
                String price = text.substring(start, text.indexOf("元"));
                if (TextUtils.isEmpty(price)) {
                    break;
                }
                ret = Float.parseFloat(price);
                if (((double) ret) > 1.0E-4d) {
                    break;
                }
                text = text.substring(start);
                index = text.indexOf(prefix);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    private String LongToString(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    private List<String> getText(Notification notification) {
        List<String> text = null;
        if (notification != null) {
            RemoteViews views = notification.bigContentView;
            if (views == null) {
                views = notification.contentView;
            }
            if (views != null) {
                text = new ArrayList<>();
                try {
                    Field field = views.getClass().getDeclaredField("mActions");
                    field.setAccessible(true);
                    Iterator it = ((ArrayList) field.get(views)).iterator();
                    while (it.hasNext()) {
                        Parcelable p = (Parcelable) it.next();
                        Parcel parcel = Parcel.obtain();
                        p.writeToParcel(parcel, 0);
                        parcel.setDataPosition(0);
                        if (parcel.readInt() == 2) {
                            parcel.readInt();
                            String methodName = parcel.readString();
                            if (methodName != null) {
                                if (methodName.equals("setText")) {
                                    parcel.readInt();
                                    text.add(((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).toString().trim());
                                }
                                parcel.recycle();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }

    public static boolean isNotificationListenerEnabled(Context context) {
        if (NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    public static void openNotificationListenSettings(Context context) {
        Intent intent;
        try {
            if (VERSION.SDK_INT >= 22) {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
