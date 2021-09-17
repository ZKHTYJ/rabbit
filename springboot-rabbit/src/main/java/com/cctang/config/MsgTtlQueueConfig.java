package com.cctang.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @program: rabbit
 * @description: 增加一个队列QC，无过期时间，也叫通用队列
 * @author: cctang
 * @create: 2021-09-17 08:55
 **/

@Configuration
public class MsgTtlQueueConfig {
//    // 定义QC队列
//    public static final String QUEUE_C="QC";
//    // 死信交换机
//    public static final String Y_DEAD__EXCHANGE="Y";
//    // 定义交换机
//    public static final String X_EXCHANGE="X";
//
//    //申明队列
//    @Bean("queueC")
//    public Queue queueC(){
//        HashMap<String, Object> arguments = new HashMap<>();
//        // 设置死信交换机
//        arguments.put("x-dead-letter-exchange",Y_DEAD__EXCHANGE);
//        // 设置死信routingKey
//        arguments.put("x-dead-letter-routing-key","YD");
//        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
//    }
//   //声明普通交换机
//   @Bean("xExchange")
//   public DirectExchange xExchange(){
//       return new DirectExchange(X_EXCHANGE);
//   }
//
//   //绑定QC与X
//    @Bean
//   public Binding ququeCBindX(@Qualifier("queueC") Queue queueC,
//                              @Qualifier("xExchange") DirectExchange xExchange){
//       return BindingBuilder.bind(queueC).to(xExchange).with("XC");
//   }
}
