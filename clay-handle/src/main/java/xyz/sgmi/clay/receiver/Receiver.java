package xyz.sgmi.clay.receiver;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import xyz.sgmi.clay.domain.AnchorInfo;
import xyz.sgmi.clay.domain.LogParam;
import xyz.sgmi.clay.enums.AnchorState;
import xyz.sgmi.clay.pending.Task;
import xyz.sgmi.clay.pending.TaskPendingHolder;
import xyz.sgmi.clay.pojo.TaskInfo;
import xyz.sgmi.clay.utils.GroupIdMappingUtils;
import xyz.sgmi.clay.utils.LogUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 * 消费MQ的消息
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Receiver {
    private static final String LOG_BIZ_TYPE = "Receiver#consumer";

    private final ApplicationContext context;

    private final TaskPendingHolder taskPendingHolder;

    private final LogUtils logUtils;

    public Receiver(ApplicationContext context, TaskPendingHolder taskPendingHolder, LogUtils logUtils) {
        this.context = context;
        this.taskPendingHolder = taskPendingHolder;
        this.logUtils = logUtils;
    }


    @KafkaListener(topics = "#{'${clay.business.topic.name}'}")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String topicGroupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            List<TaskInfo> taskInfoLists = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            String messageGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(taskInfoLists.get(0));

            /**
             * 每个消费者组 只消费 他们自身关心的消息
             */
            if (topicGroupId.equals(messageGroupId)) {
                for (TaskInfo taskInfo : taskInfoLists) {
                    logUtils.print(LogParam.builder().bizType(LOG_BIZ_TYPE).object(taskInfo).build(), AnchorInfo.builder().ids(taskInfo.getReceiver()).businessId(taskInfo.getBusinessId()).state(AnchorState.RECEIVE.getCode()).build());
                    Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
                    taskPendingHolder.route(topicGroupId).execute(task);
                }
            }
        }

    }
}
