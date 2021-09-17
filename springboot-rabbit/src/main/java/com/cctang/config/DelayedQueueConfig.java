package com.cctang.config;

import com.cctang.util.BuiltinExchangeType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @program: rabbit
 * @description: 插件  延迟队列
 * @author: cctang
 * @create: 2021-09-17 14:50
 **/
@Configuration
public class DelayedQueueConfig {
    // 交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    // 队列
    public static final String DELAYED_QUEUE_NAME = "delayed.quque";
    //routingkey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    // 申明交换机 自定义交换机 交换机类型为 x-delayed-message 基于插件
    @Bean
    public CustomExchange delayedExchange(){
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");

        /**
         * 1.名字
         * 2.类型
         * 3.是否持久化
         * 4.是否自动删除
         * 5.其它参数
         * */
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message",true,false,arguments);
    }

    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }
    @Bean
    public Binding delayedQueueBinddelayedExchange(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange") CustomExchange delayedExchange
    ){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }

}
