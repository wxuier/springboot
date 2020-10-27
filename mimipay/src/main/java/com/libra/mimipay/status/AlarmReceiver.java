package com.libra.mimipay.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.logging.Logger;

public class AlarmReceiver extends BroadcastReceiver {
    private static final Logger logger = Logger.getLogger(AlarmReceiver.class.getName());
    private static long start = System.currentTimeMillis();

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("status_service")) {
            start = System.currentTimeMillis();
            StatusInnerServiceImpl.getInstance().find();
        }
    }
}
