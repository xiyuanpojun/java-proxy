package com.liangqiang.manager.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
public class KafkaUtils {

    @SuppressWarnings("unchecked")
    private static final KafkaTemplate<String, String> kafkaTemplate = (KafkaTemplate<String, String>) SpringUtils.getBean("kafkaTemplate");

    public static void send(String channel, Object obj, ListenableFutureCallback<SendResult<String, String>> callback) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(channel, obj instanceof String ? (String) obj : JSONUtils.toJSONString(obj));
        if (callback != null) {
            future.addCallback(callback);
        }
    }

    public static void send(String channel, Object obj) {
        send(channel, obj, null);
    }

    public static SendResult<String, String> sendSync(String channel, Object obj) {
        try {
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(channel, obj instanceof String ? (String) obj : JSONUtils.toJSONString(obj));
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
