package xyz.sgmi.clay.service.deduplication.service;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.deduplication.limit.LimitService;
import xyz.sgmi.clay.enums.DeduplicationType;
import xyz.sgmi.clay.pojo.TaskInfo;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 * 频次去重服务
 */
@Service
public class FrequencyDeduplicationService extends AbstractDeduplicationService {


    @Autowired
    public FrequencyDeduplicationService(@Qualifier("SimpleLimitService") LimitService limitService) {

        this.limitService = limitService;
        deduplicationType = DeduplicationType.FREQUENCY.getCode();

    }

    private static final String PREFIX = "FRE";

    /**
     * 业务规则去重 构建key
     * <p>
     * key ： receiver + templateId + sendChannel
     * <p>
     * 一天内一个用户只能收到某个渠道的消息 N 次
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return PREFIX + StrUtil.C_UNDERLINE
                + receiver + StrUtil.C_UNDERLINE
                + taskInfo.getMessageTemplateId() + StrUtil.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}
