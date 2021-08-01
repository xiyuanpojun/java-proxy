package com.liangqiang.manager.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Consumer {

    @KafkaListener(topics = {"t0", "t1"})
    public void doTask(ConsumerRecord<String, String> record) {
        String value = record.value();
        log.info(value);
    }

}
