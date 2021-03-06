package com.cctang.consumer;

import com.cctang.config.DelayedQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: rabbit
 * @description: 延迟的消费者 基于插件的延迟
 * @author: cctang
 * @create: 2021-09-17 15:28
 **/
@Service
@Slf4j
public class DelayeQueueConsumer {

    // 监听消息
    @RabbitListener(queues = "delayed.quque")
    public void received(Message message, Channel channel)throws Exception{
        String msg = new String(message.getBody());
        log.info("当前时间： {},收到延迟队列的消息：{}",new Date().toString(),msg);
    }
}
