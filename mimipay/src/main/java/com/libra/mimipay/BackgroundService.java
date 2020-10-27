package com.libra.mimipay;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class BackgroundService extends Service {

    public static class InnerService extends Service {
        public void onCreate() {
            super.onCreate();
            startForeground(10, new Notification());
            stopSelf();
        }

        @Nullable
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("deamon", "deamon", 4);
            NotificationManager manager = (NotificationManager) getSystemService("notification");
            if (manager != null) {
                manager.createNotificationChannel(channel);
                startForeground(10, new Builder(this, "deamon").setAutoCancel(true).setCategory(NotificationCompat.CATEGORY_SERVICE).setOngoing(true).setPriority(2).build());
            }
        } else if (VERSION.SDK_INT >= 18) {
            startForeground(10, new Notification());
            startService(new Intent(this, InnerService.class));
        } else {
            startForeground(10, new Notification());
        }
    }
}
