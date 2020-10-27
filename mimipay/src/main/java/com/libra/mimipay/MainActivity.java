package com.libra.mimipay;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.libra.mimipay.common.DeviceUUID;
import com.libra.mimipay.common.HttpUtils;
import com.libra.mimipay.common.LoginUtils;
import com.libra.mimipay.common.LoginUtils.LogoutCallback;
import com.libra.mimipay.common.OnlineLogUtils;
import com.libra.mimipay.status.AlarmStatusImpl;
import com.libra.mimipay.status.StatusService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final int CLICK_COUNTS = 5;
    private static final long CLICK_DURATION = 3000;
    public static final int MSG_UPDATE_TIME = 1;
    private static final Logger logger = Logger.getLogger(MainActivity.class.getName());
    private View contentViewGroup;
    private long[] mClickHits = new long[5];
    private long[] mLogClickHits = new long[5];
    private Handler mainHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MainActivity.this.updateData(msg.obj);
                    MainActivity.this.updateTime();
                    return;
                default:
                    return;
            }
        }
    };
    private StatusService statusService = new AlarmStatusImpl();
    private String uuid;

    /* access modifiers changed from: private */
    public void updateData(Object data) {
        logger.log(Level.INFO, "data:" + data);
        if (data instanceof JSONObject) {
            String version = ((JSONObject) data).optString("version");
            if (version != null) {
                View group = findViewById(R.id.new_app_group);
                if (!TextUtils.equals(version, getString(R.string.app_version))) {
                    group.setVisibility(0);
                    ((TextView) findViewById(R.id.new_app_version)).setText(version);
                    return;
                }
                group.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateTime() {
        ((TextView) findViewById(R.id.last_time)).setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(Calendar.getInstance().getTime()));
    }

    private void initScreen() {
        ((KeyguardManager) getSystemService("keyguard")).newKeyguardLock("keyguard").disableKeyguard();
        WakeLock wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(536870913, "ZAK");
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    private void init() {
        if (HttpUtils.getMode()) {
            findViewById(R.id.model).setVisibility(0);
        }
        startService(new Intent(this, BackgroundService.class));
        this.mainHandler.postDelayed(new Runnable() {
            public void run() {
                if (!NotifyService.isNotificationListenerEnabled(MainActivity.this)) {
                    NotifyService.openNotificationListenSettings(MainActivity.this);
                }
            }
        }, 2000);
        this.uuid = DeviceUUID.getUUID(this);
        initScreen();
        MimiPayApplication application = (MimiPayApplication) getApplication();
        this.statusService.start(this, ((MimiPayApplication) getApplication()).getToken(), this.uuid, this.mainHandler);
        ((TextView) findViewById(R.id.username)).setText(application.getUsername());
        ((TextView) findViewById(R.id.balance)).setText(application.getBalance());
        ((TextView) findViewById(R.id.device_name)).setText(application.getDeviceName());
        findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LoginUtils.logout(MainActivity.this, ((MimiPayApplication) MainActivity.this.getApplication()).getToken(), new LogoutCallback() {
                    public void onLogoutResult(boolean success) {
                        if (success) {
                            MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            MainActivity.this.finish();
                            return;
                        }
                        Toast.makeText(MainActivity.this, "退出登录失败", 0).show();
                    }
                });
            }
        });
        updateTime();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(1);
        setContentView((int) R.layout.activity_main);
        setStatusBarFullTransparent();
        setFitSystemWindow(false);
        setContentView((int) R.layout.activity_main);
        init();
        findViewById(R.id.title).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.doClickCount();
            }
        });
        findViewById(R.id.user).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.doLogClickCount();
            }
        });
    }

    /* access modifiers changed from: private */
    public void doClickCount() {
        System.arraycopy(this.mClickHits, 1, this.mClickHits, 0, this.mClickHits.length - 1);
        this.mClickHits[this.mClickHits.length - 1] = SystemClock.uptimeMillis();
        if (this.mClickHits[0] >= SystemClock.uptimeMillis() - CLICK_DURATION) {
            this.mClickHits = new long[5];
            if (HttpUtils.toggleMode(this)) {
                Toast.makeText(this, "切换到调试模式", 1).show();
            } else {
                Toast.makeText(this, "切换到发布模式", 1).show();
            }
            finish();
        }
    }

    /* access modifiers changed from: private */
    public void doLogClickCount() {
        System.arraycopy(this.mLogClickHits, 1, this.mLogClickHits, 0, this.mLogClickHits.length - 1);
        this.mLogClickHits[this.mLogClickHits.length - 1] = SystemClock.uptimeMillis();
        if (this.mLogClickHits[0] >= SystemClock.uptimeMillis() - CLICK_DURATION) {
            this.mLogClickHits = new long[5];
            OnlineLogUtils.upload(this);
        }
    }

    private void startup() {
        ComponentName chatService = new ComponentName("com.feifei.example", "com.feifei.example.ChatService");
        Intent intent = new Intent();
        intent.setComponent(chatService);
        startService(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        this.statusService.stop();
        ((MimiPayApplication) getApplication()).clear();
    }

    public void setStatusBarFullTransparent() {
        if (VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(67108864);
            window.getDecorView().setSystemUiVisibility(1280);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        } else if (VERSION.SDK_INT >= 19) {
            getWindow().addFlags(67108864);
        }
    }

    public void setFitSystemWindow(boolean fitSystemWindow) {
        if (this.contentViewGroup == null) {
            this.contentViewGroup = ((ViewGroup) findViewById(16908290)).getChildAt(0);
        }
        this.contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }
}
