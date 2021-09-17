package com.cctang.rabbitmq.seven;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: rabbit
 * @description: 发消息
 * @author: cctang
 * @create: 2021-09-15 11:44
 **/
public class EitLogTopic {
    private static final String EXCHANGE_NAME="topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        HashMap<String, String> bindingMap = new HashMap<>();

        bindingMap.put("quick.orange.rabbit","被队列Q1Q2接收到");
        bindingMap.put("lazy.orange.elephant","被队列Q1Q2接收到");
        bindingMap.put("quick.orange.fox","被队列Q1接收到");
        bindingMap.put("lazy.brown.fox","被队列Q2接收到");
        bindingMap.put("lazy.pink.rabbit","虽然满足两个绑定，但只被Q2接收一次");
        bindingMap.put("quick.brown.fox","不匹配任何绑定，被丢弃");
        bindingMap.put("quick.orange.male.rabbit","四个单词，不匹配任何绑定，被丢弃");
        bindingMap.put("lazy.orange.male.rabbit","四个单词，匹配Q2");


        for (Map.Entry<String, String> entryKey : bindingMap.entrySet()) {
            String routingKey = entryKey.getKey();
            String message = entryKey.getValue();
            /**
             * @description 发送一个消费
             * 1. 发送到哪个交换机上
             * 2. 路由的key值是哪个 本次的队列名称
             * 3. 其他参数
             * 4. 发送消息的消息体
             * */
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息： "+ message);

        }
    }
}
