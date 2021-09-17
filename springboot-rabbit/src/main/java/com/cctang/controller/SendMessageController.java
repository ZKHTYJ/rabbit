package com.cctang.controller;

import com.cctang.config.DelayedQueueConfig;
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


    //优化的延迟队列 可以自定义延迟时间
    @RequestMapping("/sendExpireMsg/{message}/{ttlTime}")
    public void sendMessage(@PathVariable String message, @PathVariable String ttlTime){
        log.info("当前时间：{}，发送一条时长{}msTTL信息给队列C：{}",new Date().toString(),ttlTime,message);

        rabbitTemplate.convertAndSend("X","XC",message,msg->{
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    // 发消息 基于插件的
    @RequestMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime){
        log.info("当前时间：{}，发送一条时长{}ms信息给延迟队列delayed.queue：{}",new Date().toString(),delayTime,message);

        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,DelayedQueueConfig.DELAYED_ROUTING_KEY,message, msg->{
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    }
}
