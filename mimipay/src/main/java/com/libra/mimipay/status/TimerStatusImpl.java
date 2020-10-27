package com.libra.mimipay.status;

import android.content.Context;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;

public class TimerStatusImpl implements StatusService {
    /* access modifiers changed from: private */
    public StatusService statusService = StatusInnerServiceImpl.getInstance();
    private Timer timer;

    public void start(Context context, String token, String uuid, Handler handler) {
        this.statusService.start(context, token, uuid, handler);
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            public void run() {
                ((StatusInnerServiceImpl) TimerStatusImpl.this.statusService).find();
            }
        }, 1000, 30000);
    }

    public void stop() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.statusService.stop();
    }
}
