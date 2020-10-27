package com.libra.mimipay.common;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Handler;

import androidx.core.app.NotificationCompat;

import android.widget.Toast;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpUtils {

    public static final String BASE_URL_DEBUG = "https://192.168.2.138:8443";
    public static final String BASE_URL_PROD = "https://www.mimipay.cc";
    public static String BASE_URL = BASE_URL_PROD;

    public interface HttpCallback {
        void onFailure(int i, Object obj);

        void onSuccess(Object obj);
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        private TrustAllHostnameVerifier() {
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static boolean toggleMode(Context context) {
        boolean ret;
        if (BASE_URL == BASE_URL_DEBUG) {
            BASE_URL = BASE_URL_PROD;
            ret = false;
        } else {
            BASE_URL = BASE_URL_DEBUG;
            ret = true;
        }
        Editor editor = context.getSharedPreferences("data", 0).edit();
        editor.putBoolean("dev", ret);
        editor.commit();
        return ret;
    }

    public static void setMode(boolean debug) {
        if (debug) {
            BASE_URL = BASE_URL_DEBUG;
        } else {
            BASE_URL = BASE_URL_PROD;
        }
    }

    public static boolean getMode() {
        return BASE_URL == BASE_URL_DEBUG;
    }

    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static TrustManager[] getTrustManager() {
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
    }

    public static void post(final Context context, String url, JSONObject json, final HttpCallback cb) {
        final Handler mainHandler = new Handler(context.getMainLooper());
        new Builder()
                .sslSocketFactory(getSSLSocketFactory())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build()
                .newCall(
                        new Request.Builder()
                                .url(BASE_URL + url)
                                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json))).build())
                .enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (cb != null) {
                                            cb.onFailure(-10, "访问网络失败");
                                            OnlineLogUtils.log("访问网络失败！");
                                        }
                                        Toast.makeText(context, "访问网络失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    try {
                                        JSONObject obj = new JSONObject(response.body().string());
                                        final int status = obj.optInt(NotificationCompat.CATEGORY_STATUS);
                                        final Object data = obj.opt("data");
                                        mainHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (cb == null) {
                                                    return;
                                                }
                                                if (status < 0) {
                                                    cb.onFailure(status, data);
                                                } else if (status > 0) {
                                                    cb.onSuccess(data);
                                                }
                                            }
                                        });
                                    } catch (JSONException e3) {
                                        e3.printStackTrace();
                                    } catch (IOException e4) {
                                        e4.printStackTrace();
                                    }
                                }
                            }
                        });
    }
}
