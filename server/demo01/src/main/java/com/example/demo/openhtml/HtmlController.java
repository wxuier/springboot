package com.example.demo.openhtml;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//依赖于 org.springframework.boot:spring-boot-starter-thymeleaf

@Controller
public class HtmlController {
    @GetMapping("/index")
    public String say(){
        return "index";
    }
    @GetMapping("/index1")
    @ResponseBody
    public String say1(){
        return "index";
    }
}
