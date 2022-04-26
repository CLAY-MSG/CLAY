package xyz.sgmi.clay.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka工具类
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Component
@Slf4j
public class KafkaUtils {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 发送kafka消息
     *
     * @param topicName
     * @param jsonMessage
     */
    public void send(String topicName, String jsonMessage) {
        kafkaTemplate.send(topicName, jsonMessage);
    }

}

