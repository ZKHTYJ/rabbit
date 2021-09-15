package com.cctang.rabbitmq.six;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @program: rabbit
 * @description: 接收消息
 * @author: cctang
 * @create: 2021-09-15 10:28
 **/
public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME="direct_logs";
    public static final String QUQUE_NAME="console";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        // 申明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);


        /**
         * @description 生成队列 queueDeclare方法参数详解
         * 1. 队列名称
         * 2. 队列是否持久化（磁盘存储） 默认存在内存中
         * 3. 改队列是否只提供一个消费者（false 一个消费者） 是否进行消费共享 (true  多个)
         * 4. 是否自动删除 最后一个消费者断开连接以后 是否自动删除
         * 5. 其他参数
         * */
        channel.queueDeclare(QUQUE_NAME,false,false,false,null);
        System.out.println("ReceiveLogsDirect01等待接收消息，把接收的消息打印在屏幕上");
        channel.queueBind(QUQUE_NAME,EXCHANGE_NAME,"info");
        channel.queueBind(QUQUE_NAME,EXCHANGE_NAME,"warning");


        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println(message);
            System.out.println("ReceiveLogsDirect01控制台打印接收的消息："+new String(message.getBody(),"UTF-8"));

        };

        channel.basicConsume(QUQUE_NAME,true,deliverCallback,consumerTag->{});
    }
}
