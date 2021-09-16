package com.cctang.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author cctang
 * @version 1.0
 * @date 2021/9/16 21:32
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    @Resource
    private RabbitTemplate rabbitTemplate;
    //发消息
    @RequestMapping("/sendMessage/{message}")
    public void sendMessage( @PathVariable String message){
        log.info("当前时间：{}，发送一条信息给两个ttl队列：{}",new Date().toString(),message);

        rabbitTemplate.convertAndSend("X","XA","消息来自于ttl为10s的队列: "+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自于ttl为40s的队列: "+message);


    }
}
