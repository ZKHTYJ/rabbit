package com.cctang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @program: rabbit
 * @description: 失败回调
 * @author: cctang
 * @create: 2021-09-22 15:11
 **/
@Service
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {
    /**
     * 交换机确认回调方法
     * 1.发消息 交换机接收到了 回调
     *  1.1 correlationData 保存回调消息的ID及相关信息
     *  1.2 交换机收到消息
     *  1.3 cause 失败原因 null
     * 2. 发消息 交换机接收失败 回调
     *  2.1 correlationData 保存回调消息的ID及相关信息
     *  2.2 交换机收到消息 ack = false
     *  2.3 cause 失败原因
     * */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData!=null ? correlationData.getId() : "";


        if(ack){
            log.info("交换机已经收到 id 为:{}的消息",id);
        }else{
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}",id,cause);
        }
    }

    // 在当前消息传递过程中不可达到目的地时返回给生产者
    //至于不可达才回调
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息:{}被服务器退回，退回原因:{}, 交换机是:{}, 路由 key:{}",
                new String(message.getBody()), replyText,exchange,routingKey);
    }
}
