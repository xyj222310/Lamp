package com.yjtse.web;

import com.yjtse.service.job.QuartzManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by XieYingjie on 2017/5/14/0014.
 */

public class test {

    @Autowired
    private QuartzManager quartzManager;


    public static void main(String[] args) throws BeansException {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring-quartz.xml");
        QuartzManager quartzManager = (QuartzManager) ctx.getBean("quartzManager");

//        quartzManager.startJobs();
        System.out.println("start/.........");

    }
}