package com.hoby.kafka;

import com.hoby.constant.KafkaConstant;
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

    @KafkaListener(topics = KafkaConstant.MY_TOPIC_NAME)
    public void topicListener1(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String key = record.key();
        String value = record.value();
        log.info("key: {}, value: {}, record: {}", key, value, record);
        ack.acknowledge();
    }
}
