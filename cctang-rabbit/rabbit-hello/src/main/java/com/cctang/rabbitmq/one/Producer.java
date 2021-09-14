package com.cctang.rabbitmq.one;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author cctang
 * @version 1.0
 * @date 2021/9/11 18:46
 * @description 生产者 发消息
 */
@Slf4j
public class Producer {
    // 队列名称
    public static final String QUEUE_NAME = "ZKHTYJ";

    // 发消息
    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        /**
         * @description 生成队列 queueDeclare方法参数详解
         * 1. 队列名称
         * 2. 队列里面的消息是否持久化（磁盘存储） 默认存在内存中
         * 3. 改队列是否只提供一个消费者（false 一个消费者） 是否进行消费共享 (true  多个)
         * 4. 是否自动删除 最后一个消费者断开连接以后 是否自动删除
         * 5. 其他参数
         * */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发消息
        String message = "HELLO"; // 初次使用
        /**
         * @description 发送一个消费
         * 1. 发送到哪个交换机上
         * 2. 路由的key值是哪个 本次的队列名称
         * 3. 其他参数
         * 4. 发送消息的消息体
         * */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");

    }
}
