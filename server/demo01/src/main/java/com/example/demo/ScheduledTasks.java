package com.example.demo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Scheduled(fixedRate = 2 * 1000)
    public void runFixRate(){
        System.out.println("runFixRate");
    }

    @Scheduled(fixedDelay = 2 * 1000)
    public void runFixDelay(){
        System.out.println("runFixDelay");
    }

    @Scheduled(initialDelay = 1000, fixedRateString = "pt10s")
    public void runDelayAndRate(){
        System.out.println("runDelayAndRate");
    }

    @Scheduled(initialDelayString = "pt1s", fixedRateString = "p0dt0h0m10s")
    public void runDelayAndRate2(){
        System.out.println("runDelayAndRate2");
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void runAtMidNight(){
        System.out.println("runAtMidNight");
    }
}
