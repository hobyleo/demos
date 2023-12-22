package com.hoby.producer;

import com.hoby.constant.Constants;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author hoby
 * @since 2023-11-04
 */
public class ProducerSample {

    public static void main(String[] args) throws Exception {
        // producerSend();
        // producerSyncSend();
        // producerSendWithCallback();
        producerSendWithPartitioner();
    }

    /**
     * Producer 自定义分区器演示
     */
    public static void producerSendWithPartitioner() {
        Properties properties = getProperties();
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "top.hobyy.producer.SamplePartitioner");
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // 消息对象 -> ProducerRecord
        for (int i = 0; i < 10; i++) {
            String key = "key - " + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(Constants.TOPIC_NAME, key, "value - " + i);
            producer.send(record, (metadata, exception) -> {
                System.out.println("key = " + key + ", partition = " + metadata.partition() + ", offset = " + metadata.offset());
            });
        }

        // 关闭通道
        producer.close();

    }

    /**
     * Producer 异步回调发送演示
     */
    public static void producerSendWithCallback() {
        KafkaProducer<String, String> producer = getKafkaProducer();

        // 消息对象 -> ProducerRecord
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(Constants.TOPIC_NAME, "key - " + i, "value - " + i);
            producer.send(record, (metadata, exception) -> {
                System.out.println("partition = " + metadata.partition() + ", offset = " + metadata.offset());
            });
        }

        // 关闭通道
        producer.close();

    }

    /**
     * Producer 同步发送（异步阻塞发送）演示
     */
    public static void producerSyncSend() throws ExecutionException, InterruptedException {
        KafkaProducer<String, String> producer = getKafkaProducer();

        // 消息对象 -> ProducerRecord
        for (int i = 0; i < 10; i++) {
            String key = "key - " + i;
            String value = "value - " + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(Constants.TOPIC_NAME, key, value);
            Future<RecordMetadata> future = producer.send(record);
            RecordMetadata recordMetadata = future.get();
            System.out.println(key + ", partition = " + recordMetadata.partition() + ", offset = " + recordMetadata.offset());
        }

        // 关闭通道
        producer.close();

    }

    /**
     * Producer 异步发送演示
     */
    public static void producerSend() {
        KafkaProducer<String, String> producer = getKafkaProducer();

        // 消息对象 -> ProducerRecord
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(Constants.TOPIC_NAME, "key - " + i, "value - " + i);
            producer.send(record);
        }

        // 关闭通道
        producer.close();

    }

    private static KafkaProducer<String, String> getKafkaProducer() {
        Properties properties = getProperties();

        return new KafkaProducer<>(properties);
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.16.200:9092");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "0");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");

        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

}
