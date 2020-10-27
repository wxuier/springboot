package com.example.girl.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class GirlService {

    @Autowired
    GrilResposiroey resposiroey;

    @Transactional
    public void insertTwo(){
        Girl girlA = new Girl();
        girlA.setAge(22);
        girlA.setCupSize("B");
        resposiroey.save(girlA);

        Girl girlB = new Girl();
        girlB.setAge(21);
        girlB.setCupSize("DDD");
        resposiroey.save(girlB);
    }
}
