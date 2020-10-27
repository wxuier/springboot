package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller  //将@Controller替换成@RestController，这样不用在每个方法上都添加@ResponseBody了。
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/home")
    public String home(Model model){

        logger.warn("home page now");

        String[] cities = {"BeiJing", "ShangHai"};
        model.addAttribute("list", cities);
        model.addAttribute("title", "Big City");
        return "home";
    }


    @RequestMapping("/api")
//    @ResponseBody
    public String api(){
        return "Hello world!";
    }

    @Autowired private ObjectMapper objectMapper;

    @RequestMapping("/player")
    @ResponseStatus(HttpStatus.CREATED)
    public ObjectNode player(){
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", "jqwang");
        node.put("team", "sdk");
        return node;
    }

    //http://localhost:8080/player2
    @RequestMapping("/player2")
    public ResponseEntity<Player> player2(){
        Player player = new Player();
        player.setName("JiQing");
        player.setTeam("SDK");
        return ResponseEntity.status(HttpStatus.OK).body(player);
    }
}
