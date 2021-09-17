package com.cctang.rabbitmq.eight;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;

/**
 * @program: rabbit
 * @description: 死信发消息
 * @author: cctang
 * @create: 2021-09-15 15:38
 **/
public class DeadLettetProducter {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // 死信消息 设置TTL
        // AMQP.BasicProperties expiration = new AMQP.BasicProperties().builder().expiration("10000").build();  模拟队列达到最大容量 在c1设置


        for (int i = 1; i < 11; i++) {
            String message = i + "";
            // channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",expiration,message.getBytes());
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());
            System.out.println("生产者发出消息： "+ message);
        }
    }
}
