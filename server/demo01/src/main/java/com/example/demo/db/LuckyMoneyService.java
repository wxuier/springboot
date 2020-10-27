package com.example.demo.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class LuckyMoneyService {

    @Autowired LuckyMoneyRepository repository;

    @Transactional
    public void createTwo(){
        LuckyMoney luckyMoney1 = new LuckyMoney();
        luckyMoney1.setProducer("jqwang");
        luckyMoney1.setMoney(BigDecimal.valueOf(520));
        repository.save(luckyMoney1);

        LuckyMoney luckyMoney2 = new LuckyMoney();
        luckyMoney2.setProducer("jqwang");
        luckyMoney2.setMoney(BigDecimal.valueOf(1314));
        repository.save(luckyMoney2);
    }
}
