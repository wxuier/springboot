package com.libra.mimipay.status;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.core.app.NotificationCompat;

public class AlarmStatusImpl implements StatusService {
    private Context context;
    private PendingIntent sender;
    private StatusService statusService = StatusInnerServiceImpl.getInstance();

    public void start(Context context2, String token, String uuid, Handler handler) {
        this.statusService.start(context2, token, uuid, handler);
        this.context = context2;
        Intent intent = new Intent(context2, AlarmReceiver.class);
        intent.setAction("status_service");
        this.sender = PendingIntent.getBroadcast(context2, 0, intent, 0);
        ((AlarmManager) context2.getSystemService(NotificationCompat.CATEGORY_ALARM)).setRepeating(0, System.currentTimeMillis() + 1000, 30000, this.sender);
    }

    public void stop() {
        ((AlarmManager) this.context.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(this.sender);
        this.statusService.stop();
    }
}
