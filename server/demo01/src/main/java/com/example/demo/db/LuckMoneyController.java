package com.example.demo.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class LuckMoneyController {

    @Autowired
    private LuckyMoneyRepository repository;

    @Autowired
    LuckyMoneyService service;
    /**
     * 获取红包列表
     */
    @GetMapping("/luckymonkeys")
    public List<LuckyMoney> list(){
        return repository.findAll();
    }

    /**
     * 创建红包
     */
    @PostMapping("/luckymonkeys")
    public LuckyMoney create(@RequestParam("producer") String producer,
                             @RequestParam("money") BigDecimal money){

        LuckyMoney luckyMoney = new LuckyMoney();
        luckyMoney.setProducer(producer);
        luckyMoney.setMoney(money);
        return repository.save(luckyMoney);
    }

    /**
     * 通过ID查询红包
     */
    @GetMapping("/luckymonkeys/{id}")
    public LuckyMoney query(@PathVariable("id") int id){
        return repository.findById(id).orElse(null);
    }

    /**
     * 更新红包
     */
    @PutMapping("/luckymonkeys/{id}")
    public LuckyMoney update(@PathVariable("id") int id, @RequestParam("consumer") String consumer){

        Optional<LuckyMoney> optional = repository.findById(id);
        if(optional.isPresent()){
            LuckyMoney luckyMoney = optional.get();
            luckyMoney.setId(id);
            luckyMoney.setConsumer(consumer);
            return repository.save(luckyMoney);
        }
        return null;
    }

    @PostMapping("/luckymoneys/two")
    public void createTwo(){
        service.createTwo();
    }
}
