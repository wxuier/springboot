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

    public void getAge(Integer id) throws Exception {
        Girl girl = resposiroey.getOne(id);
        Integer age = girl.getAge();

        if(age <= 10){
            throw new GirlException(100, "你还在上小学吧");
        }
        else if(age < 60){
            throw new GirlException(101, "你大于10岁了");
        }
    }

    public Girl findOne(Integer id){
        return  resposiroey.getOne(id);
    }
}
