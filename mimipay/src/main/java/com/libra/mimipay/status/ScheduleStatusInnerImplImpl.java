package com.libra.mimipay.status;

import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;

public class ScheduleStatusInnerImplImpl implements StatusService {
    private static final int JOB_ID = 1;
    JobScheduler jobScheduler;

    public void start(Context context, String token, String uuid, Handler handler) {
        if (VERSION.SDK_INT >= 21) {
        }
        this.jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        Builder builder = new Builder(1, new ComponentName(context.getPackageName(), StatusJobScheduler.class.getName()));
        builder.setPeriodic(3000);
        if (this.jobScheduler.schedule(builder.build()) <= 0) {
        }
    }

    public void stop() {
        this.jobScheduler.cancel(1);
    }
}
