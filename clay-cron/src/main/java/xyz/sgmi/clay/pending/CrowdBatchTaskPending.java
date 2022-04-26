package xyz.sgmi.clay.pending;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import xyz.sgmi.clay.constants.PendingConstant;
import xyz.sgmi.clay.domain.BatchSendRequest;
import xyz.sgmi.clay.domain.CrowdInfoVo;
import xyz.sgmi.clay.domain.MessageParam;
import xyz.sgmi.clay.enums.BusinessCode;
import xyz.sgmi.clay.service.SendService;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 批量处理任务信息
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Component
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CrowdBatchTaskPending extends AbstractLazyPending<CrowdInfoVo> {

    @Autowired
    private SendService sendService;

    public CrowdBatchTaskPending() {
        PendingParam<CrowdInfoVo> pendingParam = new PendingParam<>();
        pendingParam.setNumThreshold(PendingConstant.NUM_THRESHOLD)
                .setQueue(new LinkedBlockingQueue(PendingConstant.QUEUE_SIZE))
                .setTimeThreshold(PendingConstant.TIME_THRESHOLD)
                .setExecutorService(ExecutorBuilder.create()
                        .setCorePoolSize(PendingConstant.CORE_POOL_SIZE)
                        .setMaxPoolSize(PendingConstant.MAX_POOL_SIZE)
                        .setWorkQueue(PendingConstant.BLOCKING_QUEUE)
                        .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                        .build());
        this.pendingParam = pendingParam;
    }

    @Override
    public void doHandle(List<CrowdInfoVo> crowdInfoVos) {

        // 1. 如果参数相同，组装成同一个MessageParam发送
        Map<Map<String, String>, String> paramMap = MapUtil.newHashMap();
        for (CrowdInfoVo crowdInfoVo : crowdInfoVos) {
            String receiver = crowdInfoVo.getReceiver();
            Map<String, String> vars = crowdInfoVo.getParams();
            if (paramMap.get(vars) == null) {
                paramMap.put(vars, receiver);
            } else {
                String newReceiver = StringUtils.join(new String[]{
                        paramMap.get(vars), receiver}, StrUtil.COMMA);
                paramMap.put(vars, newReceiver);
            }
        }

        // 2. 组装参数
        List<MessageParam> messageParams = Lists.newArrayList();
        for (Map.Entry<Map<String, String>, String> entry : paramMap.entrySet()) {
            MessageParam messageParam = MessageParam.builder().receiver(entry.getValue())
                    .variables(entry.getKey()).build();
            messageParams.add(messageParam);
        }

        // 3. 调用批量发送接口发送消息
        BatchSendRequest batchSendRequest = BatchSendRequest.builder().code(BusinessCode.COMMON_SEND.getCode())
                .messageParamList(messageParams)
                .messageTemplateId(CollUtil.getFirst(crowdInfoVos.iterator()).getMessageTemplateId())
                .build();
        sendService.batchSend(batchSendRequest);
    }
}
