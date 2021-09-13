package com.cctang.rabbitmq.one;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author cctang
 * @version 1.0
 * @date 2021/9/11 19:44
 * @description
 */
public class Consumer {
    // 队列名称
    public static final String QUEUE_NAME = "ZKHTYJ";
    // 接受消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();


        // 申明接受消息时的回调
        DeliverCallback deliverCallback = (consumerTag,message) ->{
            System.out.println(message);
            System.out.println(new String(message.getBody()));

        };

        // 取消消息时的回调
        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println("消息消费被中断");
        };
        /**
         * @description 消费者消费消息
         * 1. 消费哪个队列
         * 2. 消费成功后是否自动应答 true false：手动应答
         * 3. 消费者未成功消费的回调
         * 4. 消费着取消消费的回调
         * */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
