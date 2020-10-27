package com.example.demo.pathvar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @PostMapping("/add")
    @ResponseBody
    public String add(){
        return "users/add";
    }

    //http://localhost:8080/users/
    @GetMapping
    public String all(){
        return "userList";
    }

    //http://localhost:8080/users/32
//    @GetMapping("/{userId}")
//    @ResponseBody
//    public String showId(@PathVariable int userId){
//        return "user id is" + userId;
//    }


    //http://localhost:8080/users/jiqing
//    @GetMapping("/{userName}")
//    @ResponseBody
//    public String showName(@PathVariable("userName") String name){
//        return "user name is " + name;
//    }

    //http://localhost:8080/users/query?userId=123
//    @GetMapping("query")
//    @ResponseBody
//    public String query(@RequestParam("userId") int id, @RequestParam int userId){
//        return "query " + id + " " + userId;
//    }

    //http://localhost:8080/users/query?userId=123
    @GetMapping("query")
    @ResponseBody
    public String query(@RequestParam(value = "userId", defaultValue = "0") int id, @RequestParam Optional<Integer> userId){
        return "query " + id + " " + userId;
    }
}
