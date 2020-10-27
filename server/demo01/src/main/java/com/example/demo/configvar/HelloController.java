package com.example.demo.configvar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/say")
public class HelloController {

    @Value("${minMoney}")
    private BigDecimal minMoney;

    @Value("${description}")
    private String description;

//    http://localhost:8081/demo/say/hi
//    http://localhost:8081/demo/say/hello
    //多个映射
    @GetMapping({"/hello", "/hi"})
    public String say(){
        return "Hello world " + minMoney + " 说明:" + description;
    }

    @Autowired
    LimitConfig limitConfig;

//    http://localhost:8081/demo/say/hello1
    @GetMapping("/hello1")
    public String say1(){
        return "Hello world " + limitConfig.getMinMoney() + " 说明:" + limitConfig.getDescription();
    }

    //    http://localhost:8081/demo/say/hello1
    @PostMapping("/hello2")
    public String sayPost(){
        return "Hello world, receive post.";
    }

    //get 或 post 都可以,不推荐
    @RequestMapping("/hello3")
    public String say2(){
        return "support get or poset.";
    }
}
