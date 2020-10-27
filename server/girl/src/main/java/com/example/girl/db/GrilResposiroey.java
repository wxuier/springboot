package com.example.girl.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrilResposiroey extends JpaRepository<Girl, Integer> {

    //按年龄查询，不需要实现体
    public List<Girl> findByAge(Integer age);
}
