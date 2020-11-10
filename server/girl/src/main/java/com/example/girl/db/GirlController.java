package com.example.girl.db;

import com.example.girl.aop.aspect.HttpAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class GirlController {
    private final static Logger logger = LoggerFactory.getLogger(GirlController.class);

    @Autowired
    GrilResposiroey resposiroey;

    /**
     * 查询所有女生列表
     * @return
     */
    @GetMapping("/girls")
    public List<Girl> list(){
//        System.out.println("Girlcontroller list is exec.");
        logger.info("Girlcontroller list is exec.");
        return resposiroey.findAll();
    }

    @PostMapping("/girls")
    public Girl girlAdd(@RequestParam("cupSize") String cupSize, @RequestParam("age") int age){
        Girl girl = new Girl();
        girl.setAge(age);
        girl.setCupSize(cupSize);
        return resposiroey.save(girl);
    }

    //POST http://localhost:8080/girl/girls1?age=33&cupSize=F
    @PostMapping("/girls1")
    public Result<Girl> girlAdd(@Valid Girl girl, BindingResult bindingResult){
        Result<Girl> result = new Result<>();
        if(bindingResult.hasErrors()){
            return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
        }

        return ResultUtil.success(resposiroey.save(girl));
    }

    @GetMapping("/girls/{id}")
    public Girl query(@PathVariable("id") Integer id){
        return resposiroey.getOne(id);
    }

    @PutMapping("/girls/{id}")
    public Girl update(@PathVariable("id") Integer id,
                       @RequestParam("cupSize") String cupSize,
                       @RequestParam("age") Integer age){

        Girl girl = resposiroey.getOne(id);

        girl.setCupSize(cupSize);
        girl.setAge(age);
        return resposiroey.save(girl);
    }

    @DeleteMapping("/girls/{id}")
    public void delete(@PathVariable("id") Integer id){
        resposiroey.deleteById(id);
    }

    @GetMapping("/girllist/{age}")
    public List<Girl> query(@PathVariable("age") int age){
        return resposiroey.findByAge(age);
    }

    @Autowired
    GirlService service;

    @GetMapping("/girls/two")
    public void girlTwo(){
        service.insertTwo();
    }

    @GetMapping("/girls/getage/{id}")
    public void getAge(@PathVariable("id") Integer id) throws Exception {
        service.getAge(id);
    }
}
