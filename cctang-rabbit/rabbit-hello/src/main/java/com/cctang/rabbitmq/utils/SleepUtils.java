package com.cctang.rabbitmq.utils;

/**
 * @author cctang
 * @version 1.0
 * @date 2021/9/14 14:46
 * @description 睡眠工具类
 */
public class SleepUtils {
    public static void sleep(int second){
        try {
            Thread.sleep(1000*second);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
