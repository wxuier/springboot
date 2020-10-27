package com.libra.mimipay.common;

import android.content.Context;
import android.widget.Toast;
import com.libra.mimipay.common.HttpUtils.HttpCallback;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONObject;

public class OnlineLogUtils {
    private static int MAX_COUNT = 20;
    /* access modifiers changed from: private */
    public static Queue<Item> logs = new ConcurrentLinkedQueue();

    private static class Item {
        public String log;
        public long time = System.currentTimeMillis();

        public Item(String log2) {
            this.log = log2;
        }

        @Override
        public String toString() {
            return String.format("{\"log\":\"%s\", \"time\":%d}", new Object[]{this.log, Long.valueOf(this.time)});
        }
    }

    public void setMaxCount(int maxCount) {
        MAX_COUNT = maxCount;
    }

    public static void log(String str) {
        logs.add(new Item(str));
        if (logs.size() > MAX_COUNT) {
            logs.remove();
        }
    }

    public static void logAndUpload(final Context context, String str) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", DeviceUUID.getUUID(context));
            jsonObject.put("log", String.format("[{\"log\":\"%s\", \"time\":%d}]", new Object[]{str, Long.valueOf(System.currentTimeMillis())}));
            HttpUtils.post(context, "/mobile/upload_logs", jsonObject, new HttpCallback() {
                @Override
                public void onFailure(int i, Object obj) {
                    Toast.makeText(context, "上传log失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(Object obj) {
                    Toast.makeText(context, "上传log成功", Toast.LENGTH_SHORT).show();
                    OnlineLogUtils.logs.clear();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void upload(final Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (logs.size() > 0) {
                jsonObject.put("uuid", DeviceUUID.getUUID(context));
                jsonObject.put("log", logs.toString());
                HttpUtils.post(context, "/mobile/upload_logs", jsonObject, new HttpCallback() {
                    @Override
                    public void onFailure(int i, Object obj) {
                        Toast.makeText(context, "上传log失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(Object obj) {
                        Toast.makeText(context, "上传log成功", Toast.LENGTH_SHORT).show();
                        OnlineLogUtils.logs.clear();
                    }
                });
                return;
            }
            Toast.makeText(context, "没有需要上传的log", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
