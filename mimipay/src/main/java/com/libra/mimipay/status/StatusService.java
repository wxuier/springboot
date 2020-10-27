package com.libra.mimipay.status;

import android.content.Context;
import android.os.Handler;

public interface StatusService {
    public static final int DELAY_TIME = 1000;
    public static final int NOTIFY_COUNT = 1;
    public static final int PERIOD_TIME = 30000;
    public static final int STATUS_ALIPAY = 2;
    public static final int STATUS_ALL = 3;
    public static final int STATUS_WX = 1;

    void start(Context context, String str, String str2, Handler handler);

    void stop();
}
