package com.hoby.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

import static com.hoby.constant.Constants.TOPIC_NAME;

/**
 * @author hoby
 * @since 2023-11-04
 */
public class ConsumerSample {

    public static void main(String[] args) {
        // autoCommit();
        // commitAsync();
        // commitWithPartitionAndOffset();
        // assignPartitions();
        controlOffset();
    }

    /**
     * 手动指定 offset，并提交 offset
     */
    private static void controlOffset() {
        Properties properties = getProperties();
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        TopicPartition p0 = new TopicPartition(TOPIC_NAME, 0);
        // 订阅某个 Topic 的某个 Partition
        consumer.assign(Arrays.asList(p0));
        // 手动指定 offset
        consumer.seek(p0, 70);

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
            Set<TopicPartition> partitions = records.partitions();
            for (TopicPartition partition : partitions) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                for (ConsumerRecord<String, String> record : partitionRecords) {
                    System.out.printf("partition = %d, offset = %d, key = %s, value=%s%n",
                            record.partition(), record.offset(), record.key(), record.value());
                }
                // 单个 partition 中的 offset，并进行提交
                long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
                // 服务器记录的 offset 是下一次消费的起点，而不是终点，所以需要 + 1
                offsets.put(partition, new OffsetAndMetadata(lastOffset + 1));
                consumer.commitSync(offsets);
                System.out.printf("=============== partition - %s ===============%n", partition);
            }
        }
    }

    /**
     * 手动订阅某些分区，并提交 offset
     */
    private static void assignPartitions() {
        Properties properties = getProperties();
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        TopicPartition p0 = new TopicPartition(TOPIC_NAME, 0);
        // 订阅某个 Topic 的某个 Partition
        consumer.assign(Arrays.asList(p0));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
            Set<TopicPartition> partitions = records.partitions();
            for (TopicPartition partition : partitions) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                for (ConsumerRecord<String, String> record : partitionRecords) {
                    System.out.printf("partition = %d, offset = %d, key = %s, value=%s%n",
                            record.partition(), record.offset(), record.key(), record.value());
                }
                // 单个 partition 中的 offset，并进行提交
                long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
                // 服务器记录的 offset 是下一次消费的起点，而不是终点，所以需要 + 1
                offsets.put(partition, new OffsetAndMetadata(lastOffset + 1));
                consumer.commitSync(offsets);
                System.out.printf("=============== partition - %s ===============%n", partition);
            }
        }
    }

    /**
     * 针对每个 partition 手动提交 offset
     */
    private static void commitWithPartitionAndOffset() {
        Properties properties = getProperties();
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
            Set<TopicPartition> partitions = records.partitions();
            for (TopicPartition partition : partitions) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                for (ConsumerRecord<String, String> record : partitionRecords) {
                    System.out.printf("partition = %d, offset = %d, key = %s, value=%s%n",
                            record.partition(), record.offset(), record.key(), record.value());
                }
                // 单个 partition 中的 offset，并进行提交
                long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
                // 服务器记录的 offset 是下一次消费的起点，而不是终点，所以需要 + 1
                offsets.put(partition, new OffsetAndMetadata(lastOffset + 1));
                consumer.commitSync(offsets);
                System.out.printf("=============== partition - %s ===============%n", partition);
            }
        }
    }

    /**
     * 手动提交
     */
    private static void commitAsync() {
        Properties properties = getProperties();
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("partition = %d, offset = %d, key = %s, value=%s%n",
                        record.partition(), record.offset(), record.key(), record.value());
            }
            consumer.commitAsync();
        }
    }

    /**
     * 自动提交
     */
    private static void autoCommit() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getProperties());
        consumer.subscribe(Arrays.asList(TOPIC_NAME));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("partition = %d, offset = %d, key = %s, value=%s%n",
                        record.partition(), record.offset(), record.key(), record.value());
            }
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.16.200:9092");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");

        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        return properties;
    }

}
