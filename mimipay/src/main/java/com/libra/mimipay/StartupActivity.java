package com.libra.mimipay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import com.libra.mimipay.common.HttpUtils;
import com.libra.mimipay.common.LoginUtils;
import com.libra.mimipay.common.LoginUtils.LoginCallback;
import java.util.logging.Logger;

public class StartupActivity extends AppCompatActivity {
    private static final Logger logger = Logger.getLogger(StartupActivity.class.getName());
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_startup);
        this.handler = new Handler();
        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartupActivity.this.init();
            }
        }, 500);
    }

    public void init() {
        SharedPreferences pref = getSharedPreferences("data", 0);
        String username = pref.getString("username", "");
        String password = pref.getString("password", "");
        HttpUtils.setMode(pref.getBoolean("dev", false));
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            startLogin();
        } else {
            LoginUtils.login(this, username, password, new LoginCallback() {
                @Override
                public void onLoginResult(String str) {
                    if (TextUtils.isEmpty(str)) {
                        StartupActivity.this.startLogin();
                    } else {
                        StartupActivity.this.startMain();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(268435456);
        startActivity(intent);
        finish();
    }

    /* access modifiers changed from: private */
    public void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(268435456);
        startActivity(intent);
        finish();
    }
}
