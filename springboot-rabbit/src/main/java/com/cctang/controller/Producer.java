package com.cctang.controller;

import com.cctang.config.ConfirmConfig;
import com.cctang.config.MyCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @program: rabbit
 * @description: 高级确认生产者
 * @author: cctang
 * @create: 2021-09-22 14:52
 **/
@RestController
@RequestMapping("/confirm")
@Slf4j
public class Producer {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private MyCallBack myCallBack;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(myCallBack);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(myCallBack);
    }

    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        //指定消息 id 为 1
        CorrelationData correlationData=new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData);
        log.info("发送消息内容，{}",message+ConfirmConfig.CONFIRM_ROUTING_KEY);

        CorrelationData correlationData1=new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY+"2",message+"2",correlationData1);
        log.info("发送消息内容，{}",message+ConfirmConfig.CONFIRM_ROUTING_KEY+"2");


    }
}
