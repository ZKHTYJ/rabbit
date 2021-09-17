package com.cctang.rabbitmq.seven;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @program: rabbit
 * @description: 接收消息
 * @author: cctang
 * @create: 2021-09-15 11:18
 **/
public class ReceiveTopic01 {

    private static final String EXCHANGE_NAME="topic_logs";
    private static final String QUQUE_NAME="Q1";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        channel.queueDeclare(QUQUE_NAME,false,false,false,null);

        channel.queueBind(QUQUE_NAME,EXCHANGE_NAME,"*.orange.*");
        System.out.println("ReceiveTopic01等待接收消息，把接收的消息打印在屏幕上.......");
        DeliverCallback deliverCallback01 = (consumerTag, message) ->{
            System.out.println("ReceiveLogsDirect01控制台打印接收的消息：" + new String(message.getBody(),"UTF-8")
                    + "  接收队列： "+ QUQUE_NAME
                    + "  绑定路由key： " + message.getEnvelope().getRoutingKey()
                    + "  来自交换机： " + EXCHANGE_NAME
            );

        };

        /**
         * @description 消费者消费消息
         * 1. 消费哪个队列
         * 2. 消费成功后是否自动应答 true false：手动应答
         * 3. 消费者未成功消费的回调
         * 4. 消费着取消消费的回调
         * */
        channel.basicConsume(QUQUE_NAME,false,deliverCallback01,consumerTag->{});

    }
}
