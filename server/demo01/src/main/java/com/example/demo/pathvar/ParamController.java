package com.example.demo.pathvar;

import org.springframework.web.bind.annotation.*;

@RestController
public class ParamController {
//    http://localhost:8081/demo/test/100
    @GetMapping("/test/{id}")
    String testParam(@PathVariable("id") Integer id){
        return "test param id is " + id;
    }

//    http://localhost:8081/demo/test1?id=112
    @GetMapping("/test1")
    String testParam1(@RequestParam("id") Integer myId){
        return "param Id is " + myId;
    }

//    http://localhost:8081/demo/test1?id=112
    @GetMapping("/test2")
    String testParam2(@RequestParam(value = "id", required = false, defaultValue = "10") Integer myId){
        return "param Id is " + myId;
    }
}
