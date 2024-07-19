package com.hoby.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author liaozh
 * @since 2024-07-19
 */
@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "my-topic")
    public void topicListener1(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        log.info("record: {}, value: {}", record, value);
        ack.acknowledge();
    }
}
