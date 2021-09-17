package com.cctang.rabbitmq.eight;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;

/**
 * @program: rabbit
 * @description: 死信队列实战
 * @author: cctang
 * @create: 2021-09-15 14:56
 **/
public class DeadLetterReceive01 {
    // 定义普通队列和普通交换机
    public static final String NORMAL_QUQUE = "normal_quque";
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //定义死信队列和死信交换机
    public static final String DEAD_QUQUE = "dead_quque";
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //声明两个交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);



        HashMap<String, Object> argument = new HashMap<>();
        // 过期时间 10s 放在生产者那边写
        // argument.put("x-message-ttl",10000);
        // 正常队列设置死信交换机
        argument.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        // 设置死信路由key
        argument.put("x-dead-letter-routing-key","lisi");
        //设置正常队列的长度设置  接下来演示队列拒绝接收消息
        // argument.put("x-max-length",6);


        // 声明普通队列
        channel.queueDeclare(NORMAL_QUQUE,false,false,false,argument);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////



        // 声明死信队列
        channel.queueDeclare(DEAD_QUQUE,false,false,false,null);

        //绑定普通交换机和普通队列
        channel.queueBind(NORMAL_QUQUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信交换机和死信队列
        channel.queueBind(DEAD_QUQUE,DEAD_EXCHANGE,"lisi");

        System.out.println("等待接收消息。。。。。。");
        DeliverCallback delivery =(consumertag, message) ->{
            String msg = new String(message.getBody(),"UTF-8");
            if(msg.equals("5")){
                System.out.println(new String(message.getBody(),"UTF-8" )+ ": 此消息是被拒绝的");
                /**
                 * 消息标记
                 * 不放回原普通队列
                 * */
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else{
                System.out.println("DeadLetterReceive01控制台打印接收的消息：" + new String(message.getBody(),"UTF-8"));
                /**
                 * 消息标记
                 * 非批量消费
                 * */
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }

        };    /**
         * @description 消费者消费消息
         * 1. 消费哪个队列
         * 2. 消费成功后是否自动应答 true false：手动应答
         * 3. 消费者未成功消费的回调
         * 4. 消费着取消消费的回调
         * */

        // channel.basicConsume(NORMAL_QUQUE,true,delivery ,consumertag->{});
        // 开启手动应答 自动应答就不存在拒绝问题
        channel.basicConsume(NORMAL_QUQUE,false,delivery ,consumertag->{});
    }
}
