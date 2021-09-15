package com.cctang.rabbitmq.four;

import com.cctang.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author cctang
 * @version 1.0
 * @date 2021/9/14 17:02
 * @description 发布确认模式
 * 使用方式哪种最好
 * 1. 单个确认 ==>0
 * 2. 批量确认 ==>1
 * 3. 异步批量确认 ==>2
 */
public class ConfimMessage { // 批量发消息的个数

    public static final int MESSAGE_COUNT = 1000;
    // 确认类型 0 1 2
    public static int type = 0;

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // 队列的声明
        String ququeName = UUID.randomUUID().toString();
        // 单个确认
        //ConfimMessage.publishMessageIndividually(channel,ququeName);

        // 批量确认
        //ConfimMessage.muitlMessage(channel,ququeName);

        // 异步批量确认
        ConfimMessage.syncMutilMessage(channel,ququeName);

    }

    /**
     * 1. 单个确认
     * 发布1000个单独确认个消息，耗时216ms   笔记本503ms
     * 缺点： 发布慢
     * */
    public static void publishMessageIndividually(Channel channel, String ququeName) throws Exception{


        long begin =ConfimMessage.publicMethod(channel,ququeName);
        // 批量发消息
        for (int i = 0; i< MESSAGE_COUNT; i++) {
            ConfimMessage.sendMessageMutil(channel,ququeName,i,0,null);
            // 单个消息马上发布确认
            boolean flag = channel.waitForConfirms();
            if (flag){
                System.out.println("第"+i+"号"+"消息发送成功");
            }
        }
        // 总计耗时
       ConfimMessage.cutTime(begin,0);
    }



    /**
     * 2. 批量确认
     * 发布1000个单独确认个消息，耗时46ms  笔记本102ms
     * 缺点： 无法定位哪个消息发送出问题
     * */
    public static void muitlMessage(Channel channel, String ququeName) throws Exception{
        long begin = ConfimMessage.publicMethod(channel, ququeName);
        // 批量确认消息大小
        int batchSize = 100;

        // 批量发送消息 批量确认
        for (int i=0; i< MESSAGE_COUNT; i++){
            ConfimMessage.sendMessageMutil(channel,ququeName,i,1,null);
            // 判断达到100条消息的时候 批量确认一次
            if(i%batchSize == 0) {
                channel.waitForConfirms();
            }
        }
        ConfimMessage.cutTime(begin,1);
    }

    /**
     * 3. 异步批量确认
     * 发布1000个异步批量确认个消息，耗时39ms
     * 性价比最高
     * */
    public static void syncMutilMessage(Channel channel, String ququeName) throws Exception{

        /**
         * 线程安全有序的一个哈希表 适用于高并发场景下
         * 1. 轻松的将序号和消息进行关联
         * 2. 轻松的批量删除条目 只要给序号
         * 3. 支持高并发
         * */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        /**
         * 1. deliverTag 消息的标记
         * 2. multiple 是否为批量确认
         * */
        // 消息确认成功回调
        ConfirmCallback ackCallback = (deliverTag, multiple) -> {
            if(multiple){
                // B 删除已经确认的消息 剩下的就是未确认的
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirms.headMap(deliverTag);
                confirmed.clear();
            }else{
                outstandingConfirms.remove(deliverTag);
            }

            System.out.println("确认的消息：" + deliverTag);
        };

        // 消息确认失败回调
        ConfirmCallback nackCallback= (deliverTag, multiple) -> {
            // C 打印未确认的消息有哪些
            String message = outstandingConfirms.get(deliverTag);

            System.out.println("未确认的消息是："+ message + "：：：未确认的消息tag：" + deliverTag);
        };
        //  准备消息监听器 监听哪些消息成功 哪些失败
        channel.addConfirmListener(ackCallback,nackCallback); // 异步通知
        long begin = ConfimMessage.publicMethod(channel, ququeName);

        // 批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            ConfimMessage.sendMessageMutil(channel, ququeName,i,2,outstandingConfirms);
        }
        ConfimMessage.cutTime(begin,2);
    }

    // 公共部分剥离 开始时间
    public static long publicMethod(Channel channel,String ququeName) throws Exception{
        /**
         * @description 生成队列 queueDeclare方法参数详解
         * 1. 队列名称
         * 2. 队列是否持久化（磁盘存储） 默认存在内存中
         * 3. 改队列是否只提供一个消费者（false 一个消费者） 是否进行消费共享 (true  多个)
         * 4. 是否自动删除 最后一个消费者断开连接以后 是否自动删除
         * 5. 其他参数
         * */
        channel.queueDeclare(ququeName,true,false,false,null);
        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();
        return begin;
    }
    // 时间差
    public static void cutTime(long begin,int type){
        // 结束时间
        long end = System.currentTimeMillis();
        switch (type) {
            case 0 :
                System.out.println("发布"+MESSAGE_COUNT+"个单独确认个消息，耗时" + (end-begin) + "ms");
                break;
            case 1 :
                System.out.println("发布"+MESSAGE_COUNT+"个批量确认个消息，耗时" + (end-begin) + "ms");
                break;
            case 2 :
                System.out.println("发布"+MESSAGE_COUNT+"个异步批量确认个消息，耗时" + (end-begin) + "ms");
                break;
        }

    }
    // 批量发消息
    public static void sendMessageMutil(Channel channel, String ququeName, int i,int type,ConcurrentSkipListMap<Long, String> outstandingConfirms) throws Exception{
        String message = i+ "";
        channel.basicPublish("",ququeName,null,message.getBytes());
        if(type == 2) {
            // A 将消息全部存储
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);

        }
    }

}
