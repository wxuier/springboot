package com.example.yungou;

import com.yungouos.pay.entity.CodePayBiz;
import com.yungouos.pay.wxpay.WxPay;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayController {

    @GetMapping("/qrpay")
    public String payQrCode(){
//        CodePayBiz codePayBiz2 = WxPay.getCodePayResult("1603714653988", "1603343442", "274406C4F9D243CCBD72FC9057B6E3FD");

        String result= WxPay.nativePay(System.currentTimeMillis() + "", "1", "1603343442", "测试", null, null, null, null,null,null,null,"274406C4F9D243CCBD72FC9057B6E3FD");
        return result;
    }
}
