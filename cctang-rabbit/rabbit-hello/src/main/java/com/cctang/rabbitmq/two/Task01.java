package com.cctang.rabbitmq.two;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbit
 * @description:  生成者发送大量消息
 * @author: cctang
 * @create: 2021-09-13 15:20
 **/
public class Task01 {
    // 队列名称
    public static final String QUEUE_NAME = "ZKHTYJ";

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
        // 从控制台接受代码
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.next();
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

}
