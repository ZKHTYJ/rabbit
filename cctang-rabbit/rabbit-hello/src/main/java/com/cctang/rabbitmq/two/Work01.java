package com.cctang.rabbitmq.two;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbit
 * @description: 工作线程01 这是一个消费者
 * @author: cctang
 * @create: 2021-09-13 15:09
 **/
public class Work01 {
    // 队列名称
    public static final String QUEUE_NAME = "ZKHTYJ";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        // 申明接受消息时的回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println(message);
            System.out.println(new String(message.getBody()));

        };

        // 取消消息时的回调
        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println(consumerTag + "消息消费者取消接口回调逻辑");
        };


        /**
         * @description 消费者消费消息
         * 1. 消费哪个队列
         * 2. 消费成功后是否自动应答 true false：手动应答
         * 3. 消费者未成功消费的回调
         * 4. 消费着取消消费的回调
         * */
        System.out.println("C2等待接收消息。。。。");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
