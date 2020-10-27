package com.example.girl;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
public class RequestMappingController {

    @GetMapping(value = {"/hello3", "/hi"})
    public String say3(){
        return "Hello3 或 hi都是我。";
    }

    @PostMapping("/post01")
    public String say4(){
        return "Post test";
    }

    @GetMapping("/hello4/{id}")
    public String say5(@PathVariable("id") int id){
        return "path variable is " + id;
    }

    @GetMapping("/{id}/hello4")
    public String say6(@PathVariable("id") int id){
        return "path variable is " + id;
    }

    @GetMapping("/hello4")
    public String say7(@RequestParam("id") int myId){
        return "RequestParam is " + myId;
    }

    @GetMapping("/hello5")
    public String say8(@RequestParam(value = "id", required = false, defaultValue = "1") int myId){
        return "RequestParam is " + myId;
    }
}
