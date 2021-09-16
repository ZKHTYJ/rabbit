package com.cctang.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author cctang
 * @version 1.0
 * @date 2021/9/16 21:31
 * @description 死信消费者
 */
@Slf4j
@Service
public class DeadletterQueueConsumer {


    // 接收消息
    @RabbitListener(queues = "QD")
    public void received(Message message, Channel channel)throws Exception{
        String msg = new String(message.getBody());
        log.info("当前时间： {},收到死信队列的消息：{}",new Date().toString(),msg);
    }
}
