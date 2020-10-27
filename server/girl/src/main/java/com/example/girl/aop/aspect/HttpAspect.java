package com.example.girl.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class HttpAspect {
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.example.girl.db.GirlController.list(..))")
    public void log(){

    }
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
//        System.out.println("GirlController list is aspect before.");
        logger.info("GirlController list is aspect before.");

        //url
        //method
        //ip
        //member function
        //parameter
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("url = {}", request.getRequestURL());
        logger.info("method = {}", request.getMethod());
        logger.info("ip = {}", request.getRemoteAddr());
        logger.info("class_method = {}", joinPoint.getSignature().getDeclaringTypeName()+ "." + joinPoint.getSignature().getName());
        logger.info("parameter = {}", joinPoint.getArgs());
    }

    @After("log()")
    public void doAfter(){
//        System.out.println("GirlController list is aspect after.");
        logger.info("GirlController list is aspect after.");
    }

    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object){
        logger.info("response = {}", object);
    }
}

