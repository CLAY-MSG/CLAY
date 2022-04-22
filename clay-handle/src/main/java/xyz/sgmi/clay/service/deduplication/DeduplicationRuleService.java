package xyz.sgmi.clay.service.deduplication;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.constant.ClayConstant;
import xyz.sgmi.clay.domain.DeduplicationParam;
import xyz.sgmi.clay.enums.AnchorState;
import xyz.sgmi.clay.pojo.TaskInfo;

import java.util.Date;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 * 去重服务
 */
@Service
public class DeduplicationRuleService {

    /**
     * 配置样例：{"contentDeduplication":{"num":1,"time":300},"frequencyDeduplication":{"num":5}}
     */
    private static final String DEDUPLICATION_RULE_KEY = "deduplication";
    private static final String CONTENT_DEDUPLICATION = "contentDeduplication";
    private static final String FREQUENCY_DEDUPLICATION = "frequencyDeduplication";
    private static final String TIME = "time";
    private static final String NUM = "num";

    @Autowired
    private ContentDeduplicationService contentDeduplicationService;

    @Autowired
    private FrequencyDeduplicationService frequencyDeduplicationService;

    @ApolloConfig("boss.clay")
    private Config config;

    public void duplication(TaskInfo taskInfo) {
        JSONObject property = JSON.parseObject(config.getProperty(DEDUPLICATION_RULE_KEY, ClayConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT));
        JSONObject contentDeduplication = property.getJSONObject(CONTENT_DEDUPLICATION);
        JSONObject frequencyDeduplication = property.getJSONObject(FREQUENCY_DEDUPLICATION);

        // 文案去重
        DeduplicationParam contentParams = DeduplicationParam.builder()
                .deduplicationTime(contentDeduplication.getLong(TIME))
                .countNum(contentDeduplication.getInteger(NUM)).taskInfo(taskInfo)
                .anchorState(AnchorState.CONTENT_DEDUPLICATION)
                .build();
        contentDeduplicationService.deduplication(contentParams);


        // 运营总规则去重(一天内用户收到最多同一个渠道的消息次数)
        Long seconds = (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000;
        DeduplicationParam businessParams = DeduplicationParam.builder()
                .deduplicationTime(seconds)
                .countNum(frequencyDeduplication.getInteger(NUM)).taskInfo(taskInfo)
                .anchorState(AnchorState.RULE_DEDUPLICATION)
                .build();
        frequencyDeduplicationService.deduplication(businessParams);
    }

}
