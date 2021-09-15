package com.cctang.rabbitmq.eight;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;

/**
 * @program: rabbit
 * @description: c2消费死信队列消息
 * @author: cctang
 * @create: 2021-09-15 16:01
 **/
public class DeadLetterReceive02 {

    public static final String DEAD_QUQUE = "dead_quque";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();


        System.out.println("等待接收消息。。。。。。");
        DeliverCallback delivery =(consumertag, message) ->{
            System.out.println("DeadLetterReceive02控制台打印接收的消息：" + new String(message.getBody(),"UTF-8")
            );
        };
        channel.basicConsume(DEAD_QUQUE,true,delivery ,consumertag->{});
    }
}
