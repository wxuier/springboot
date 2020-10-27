package com.libra.mimipay;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.libra.mimipay.common.LoginUtils;
import com.libra.mimipay.common.LoginUtils.LoginCallback;
import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {
    private static final Logger logger = Logger.getLogger(LoginActivity.class.getName());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_login);
        findViewById(R.id.login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.login();
            }
        });
    }

    public final void login() {
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            LoginUtils.login(this, username, password, new LoginCallback() {
                @Override
                public void onLoginResult(String str) {
                    if (TextUtils.isEmpty(str)) {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                }
            });
        }
    }
}
