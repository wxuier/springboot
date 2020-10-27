package com.libra.mimipay.status;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class StatusJobScheduler extends JobService {
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
