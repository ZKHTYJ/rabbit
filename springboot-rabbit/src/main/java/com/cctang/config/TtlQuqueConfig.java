package com.cctang.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author cctang
 * @version 1.0
 * @date 2021/9/16 21:29
 * @description 延迟队列配置 TTL队列
 */
@Configuration
public class TtlQuqueConfig {
    // 普通交换机
    public static final String X_EXCHANGE="X";
    // 死信交换机
    public static final String Y_DEAD__EXCHANGE="Y";
    //普通队列
    public static final String QUEUE_A="QA";
    public static final String QUEUE_B="QB";
    //死信队列
    public static final String DEAD_LETTER_QUEUE="QD";

    //声明普通交换机
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    //声明死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD__EXCHANGE);
    }

    //申明普通队列A ttl为10s
    @Bean("queueA")
    public Queue queueA(){
        HashMap<String, Object> arguments = new HashMap<>();
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD__EXCHANGE);
        // 设置死信routingKey
        arguments.put("x-dead-letter-routing-key","YD");
        // 设置TTL  单位ms
        arguments.put("x-message-ttl",10000);

        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //申明普通队列B ttl为10s
    @Bean("queueB")
    public Queue queueB(){
        HashMap<String, Object> arguments = new HashMap<>();
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD__EXCHANGE);
        // 设置死信routingKey
        arguments.put("x-dead-letter-routing-key","YD");
        // 设置TTL  单位ms
        arguments.put("x-message-ttl",40000);

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    // 申明死信队列
    @Bean("ququeD")
    public Queue ququeD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //绑定
    @Bean
    public Binding ququeABindX(@Qualifier("queueA") Queue queueA,
                               @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding ququeBBindX(@Qualifier("queueB") Queue queueB,
                               @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding ququeDBindY(@Qualifier("ququeD") Queue queueB,
                               @Qualifier("yExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("YD");
    }


}

