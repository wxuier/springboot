package com.example.girl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//不推荐使用加载html模板，现在一般是前后端分离
@Controller
public class TemplateController {

    @GetMapping("/temp")
    String temp(){
        return "index";
    }
}
