package xyz.sgmi.clay.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

/**
 * flink工具类
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Slf4j
public class MessageQueueUtils {


    /**
     * 获取kafkaConsumer
     * @param topicName
     * @param groupId
     * @return
     */
    public static KafkaSource<String> getKafkaConsumer(String topicName, String groupId, String broker) {
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(broker)
                .setTopics(topicName)
                .setGroupId(groupId)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        return source;
    }
}
