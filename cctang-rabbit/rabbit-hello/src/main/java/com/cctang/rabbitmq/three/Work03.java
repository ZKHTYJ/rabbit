package com.cctang.rabbitmq.three;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.cctang.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author cctang
 * @version 1.0
 * @date 2021/9/14 14:38
 * @description 消息在手动应答时不丢失 放入消息队列重新消费
 */
public class Work03 {

    // 队列名称
    public static final String TASK_QUEUE_NAME = "ack_quque";

    // 接收消息
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1等待接收消息处理时间较短");

        DeliverCallback deliverCallback = (consumerTag,message) ->{
            // 沉睡1s
            SleepUtils.sleep(1);
            System.out.println(message);
            System.out.println("C1接收道的消息为: "+new String(message.getBody(),"UTF-8"));
            // 手动应答
            /**
             * 1. 消息的标记 tag
             * 2. 是否批量应答 false true-> 批量应答
             * */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println(consumerTag + "消息消费者取消接口回调逻辑");
        };
        // 设置不公平分发
        int perfetCount =2;
        channel.basicQos(perfetCount);
        // 采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }

}
