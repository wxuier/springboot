package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class MimiPaySample {
    // 将userKey和secret修改为自己的userKey和secret
    private final static String USER_KEY = "FC86F5EC05FE6137FFE7CDEB3042E244";
    private final static String SECRET = "8672AE95AD27DCF5CA366E53D2F50763";

    // 支付时调用该函数
    public void pay(PayParams payParams, HttpServletResponse response) {
        if (null != payParams) {
            payParams.setUserKey(USER_KEY);
            payParams.setKey(genPayKey(payParams, SECRET));

            String paramsString = JSONObject.toJSONString(payParams);
            String format = "html";
            String result = HttpUtils.postData( "https://www.mimipay.cc/api/unified_order?format=" + format, paramsString);
            if (format.equals("json")) {
                // 如果是json，则为自定义模式，这里需要用户自己来对数据做处理，官方建议使用html格式（标准模式）
            } else {
                response.setContentType("text/html; charset=utf-8");
            }

            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.write(result);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    // 支付成功后，咪咪后台会通知开发者notify_url对应的控制器，在该函数中主要用于将账单写入数据库

//    建议的controller如下：
    @PostMapping("/notify_url")
    public void notifyUrl(@RequestBody NotifyParams notifyParams, HttpServletResponse response) {
        notify(notifyParams, response);
    }

    public void notify(NotifyParams notifyParams, HttpServletResponse response) {
        if (null != notifyParams) {
            String key = genNotifyKey(notifyParams, SECRET);
            if (key.equals(notifyParams.getKey())) {
                // 写账单数据到数据库
                if (writeData2DB(notifyParams)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean writeData2DB(NotifyParams notifyParams) {
        // 开发者根据自己填写业务填写
        return true;
    }

    private static String genNotifyKey(NotifyParams notifyUserParams, String secret) {
        if (!StringUtils.isEmpty(secret)) {
            StringBuilder sb = new StringBuilder();
            sb.append(notifyUserParams.getOutTradeNo());
            sb.append((int)(notifyUserParams.getPrice() * 100));
            sb.append((int)(notifyUserParams.getRealPrice() * 100));
            sb.append(secret);
            return md5(sb.toString());
        }
        return "";
    }

    private static String genPayKey(PayParams payParams, String secret) {
        if (!StringUtils.isEmpty(secret)) {
            StringBuilder sb = new StringBuilder();
            sb.append(payParams.getUserKey());
            sb.append((int)(payParams.getPrice() * 100));
            sb.append(payParams.getType());
            sb.append(payParams.getOutTradeNo());
            sb.append(payParams.getNotifyUrl());
            sb.append(secret);
            return md5(sb.toString());
        }
        return "";
    }

    private static String md5(String data) {
        String ret = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                temp = temp.toLowerCase();
                if (temp.length() == 1) {
                    sb.append("0");
                }
                sb.append(temp);
            }

            ret = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
