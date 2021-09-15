package com.cctang.rabbitmq.six;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @program: rabbit
 * @description: 发消息
 * @author: cctang
 * @create: 2021-09-15 10:57
 **/
public class DirectLogs {
    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // 从控制台输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();

            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes("UTF-8"));

            System.out.println("生产者发消息" + message);
        }
    }
}
