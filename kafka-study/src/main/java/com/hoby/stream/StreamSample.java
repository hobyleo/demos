package com.hoby.stream;

import com.hoby.constant.Constants;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

/**
 * @author hoby
 * @since 2023-11-05
 */
public class StreamSample {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.16.200:9092");
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-app");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // 构建流拓扑
        StreamsBuilder builder = new StreamsBuilder();
        // 构建 word count processor
        wordCountStream(builder);

        KafkaStreams streams = new KafkaStreams(builder.build(), properties);

        streams.start();
    }

    /**
     * 定义流计算过程
     */
    public static void wordCountStream(StreamsBuilder builder) {
        KStream<String, String> source = builder.stream(Constants.INPUT_TOPIC);

        KTable<String, Long> count = source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" ")))
                .groupBy((key, value) -> value)
                .count();

        count.toStream().to(Constants.OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
    }

}
