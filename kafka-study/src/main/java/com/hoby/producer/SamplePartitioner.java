package com.hoby.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 自定义分区器
 *
 * @author hoby
 * @since 2023-11-04
 */
public class SamplePartitioner implements Partitioner {

    /**
     * <p>决定数据进入哪一个 partition</p>
     *
     * @param topic The topic name
     * @param key The key to partition on (or null if no key)
     * @param keyBytes The serialized key to partition on( or null if no key)
     * @param value The value to partition on or null
     * @param valueBytes The serialized value to partition on or null
     * @param cluster The current cluster metadata
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        /*
           key - 1
           key - 2
           key - 3
         */
        String keyStr = String.valueOf(key);
        String keyInt = keyStr.substring(6);
        System.out.println("keyStr = " + keyStr + ", keyInt = " + keyInt);

        int i = Integer.parseInt(keyInt);
        return i % 2;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
