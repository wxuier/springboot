package com.libra.mimipay.status;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;
import com.libra.mimipay.R;
import com.libra.mimipay.common.HttpUtils;
import com.libra.mimipay.common.HttpUtils.HttpCallback;
import com.libra.mimipay.common.LoginUtils;
import com.libra.mimipay.common.LoginUtils.LoginCallback;
import com.libra.mimipay.common.OnlineLogUtils;
import java.util.logging.Logger;
import org.json.JSONObject;

public class StatusInnerServiceImpl implements StatusService, LoginCallback {
    private static final Logger logger = Logger.getLogger(StatusInnerServiceImpl.class.getName());
    private static volatile StatusInnerServiceImpl sStatusInnerImpl = null;
    protected Context context;
    protected Handler mainHandler;
    protected int notifyCount = 0;
    protected int status = 0;
    protected String token;
    protected String uuid;

    private StatusInnerServiceImpl() {
    }

    public static StatusInnerServiceImpl getInstance() {
        if (sStatusInnerImpl == null) {
            synchronized (StatusInnerServiceImpl.class) {
                if (sStatusInnerImpl == null) {
                    sStatusInnerImpl = new StatusInnerServiceImpl();
                }
            }
        }
        return sStatusInnerImpl;
    }

    public void start(Context context2, String token2, String uuid2, Handler handler) {
        this.context = context2;
        this.token = token2;
        this.uuid = uuid2;
        this.mainHandler = handler;
    }

    public void stop() {
        this.notifyCount = 0;
        this.status = 0;
    }

    public void find() {
        if (3 != this.status) {
            this.status = 3;
        }
        if (1 == 0) {
        }
        if (2 == 0) {
        }
        if (3 != 3) {
        }
        this.notifyCount++;
        if (this.notifyCount == 1) {
            this.notifyCount = 0;
            notifyServer();
        }
    }

    /* access modifiers changed from: private */
    public void reLogin() {
        LoginUtils.login(this.context, this);
    }

    private void notifyServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", this.token);
            jsonObject.put("uuid", this.uuid);
            jsonObject.put("version", this.context.getString(R.string.app_version));
            jsonObject.put(NotificationCompat.CATEGORY_STATUS, this.status);
            HttpUtils.post(this.context, "/mobile/alive", jsonObject, new HttpCallback() {
                @Override
                public void onFailure(int errorCode, Object data) {
                    if (errorCode > -10) {
                        StatusInnerServiceImpl.this.reLogin();
                    }
                }

                @Override
                public void onSuccess(Object data) {
                    StatusInnerServiceImpl.this.mainHandler.sendMessage(Message.obtain(StatusInnerServiceImpl.this.mainHandler, 1, data));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLoginResult(String str) {
        if (TextUtils.isEmpty(str)) {
            OnlineLogUtils.log("status service relogin failed");
        } else {
            this.token = str;
        }
    }
}
