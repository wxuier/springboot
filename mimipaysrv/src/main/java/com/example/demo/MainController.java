package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @RequestMapping("/")
    void home(HttpServletResponse response){

        PayParams payParams = new PayParams();

        payParams.setPrice(0.01f);
        payParams.setType(1);
        payParams.setOutTradeNo("" + System.currentTimeMillis());
        payParams.setTradeContent("travian");
        payParams.setNotifyUrl("https://www.baidu.com/");
        payParams.setOutUserNo("voidsun");
        MimiPaySample sample = new MimiPaySample();
        sample.pay(payParams, response);
    }
}
