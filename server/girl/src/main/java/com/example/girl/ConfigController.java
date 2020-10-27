package com.example.girl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    @Value("${cupSize}")
    private String cupSize;

    @Value("${age}")
    private Integer age;

    @GetMapping("/hello")
    public String say(){
        return "cupSize is " + cupSize + " age is " + age;
    }


    @Value("${content}")
    private String conent;

    @GetMapping("/hello1")
    public String say1(){
        return conent;
    }

    @Autowired GirlProperties girlProperties;
    @GetMapping("/hello2")
    public String say2(){
        return girlProperties.getCupSize();
    }
}
