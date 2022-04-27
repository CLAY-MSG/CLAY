package xyz.sgmi.clay.service.deduplication.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.sgmi.clay.deduplication.DeduplicationParam;
import xyz.sgmi.clay.deduplication.limit.LimitService;
import xyz.sgmi.clay.domain.AnchorInfo;
import xyz.sgmi.clay.pojo.TaskInfo;
import xyz.sgmi.clay.service.deduplication.DeduplicationHolder;
import xyz.sgmi.clay.service.deduplication.DeduplicationService;
import xyz.sgmi.clay.utils.LogUtils;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 * 去重服务
 */
@Slf4j
public abstract class AbstractDeduplicationService implements DeduplicationService {

    protected Integer deduplicationType;

    protected LimitService limitService;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    @PostConstruct
    private void init() {
        deduplicationHolder.putService(deduplicationType, this);
    }

    @Autowired
    private LogUtils logUtils;


    @Override
    public void deduplication(DeduplicationParam param) {
        TaskInfo taskInfo = param.getTaskInfo();

        Set<String> filterReceiver = limitService.limitFilter(this, taskInfo, param);

        // 剔除符合去重条件的用户
        if (CollUtil.isNotEmpty(filterReceiver)) {
            taskInfo.getReceiver().removeAll(filterReceiver);
            logUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId()).ids(filterReceiver).state(param.getAnchorState().getCode()).build());
        }
    }


    /**
     * 构建去重的Key
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    public abstract String deduplicationSingleKey(TaskInfo taskInfo, String receiver);


}
