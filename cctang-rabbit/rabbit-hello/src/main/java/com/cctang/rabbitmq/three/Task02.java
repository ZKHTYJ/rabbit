package com.cctang.rabbitmq.three;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbit
 * @description: 消息手动应答不丢失， 放回队列重新消费
 * @author: cctang
 * @create: 2021-09-13 15:47
 **/
public class Task02 {
    // 队列名称
    public static final String TASK_QUEUE_NAME = "ack_quque";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        // C 开启发布确认
       //  channel.confirmSelect();  // ConfimMessage类中使用
        // 声明队列
        boolean durable = true; // A 队列持久化
        /**
         * @description 生成队列 queueDeclare方法参数详解
         * 1. 队列名称
         * 2. 队列是否持久化（磁盘存储） 默认存在内存中
         * 3. 改队列是否只提供一个消费者（false 一个消费者） 是否进行消费共享 (true  多个)
         * 4. 是否自动删除 最后一个消费者断开连接以后 是否自动删除
         * 5. 其他参数
         * */
        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);

        // 从控制台输入消息
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.next();
            /**
             * @description 发送一个消费  目前为轮询 公平分发 推荐不公平分发
             * 1. 发送到哪个交换机上
             * 2. 路由的key值是哪个 本次的队列名称
             * 3. 消息持久化 （并不能保证消息完全不丢失 简单队列可以满足 后续完善）
             * 4. 发送消息的消息体
             * */
            // B 设置生产者发送消息持久化，保存到磁盘上MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
            System.out.println("生产者发出消息" + message);
        }


    }
}
