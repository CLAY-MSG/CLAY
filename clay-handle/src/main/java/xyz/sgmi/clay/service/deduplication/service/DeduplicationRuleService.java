package xyz.sgmi.clay.service.deduplication.service;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.constant.ClayConstant;
import xyz.sgmi.clay.domain.DeduplicationParam;
import xyz.sgmi.clay.enums.DeduplicationType;
import xyz.sgmi.clay.pojo.TaskInfo;
import xyz.sgmi.clay.service.deduplication.DeduplicationHolder;

import java.util.List;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 * 去重服务
 */
@Service
public class DeduplicationRuleService {

    public static final String DEDUPLICATION_RULE_KEY = "deduplication";

    @ApolloConfig("boss.clay")
    private Config config;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    public void duplication(TaskInfo taskInfo) {
        // 配置样例：{"deduplication_10":{"num":1,"time":300},"deduplication_20":{"num":5}}
        String deduplicationConfig = config.getProperty(DEDUPLICATION_RULE_KEY, ClayConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT);

        // 去重
        List<Integer> deduplicationList = DeduplicationType.getDeduplicationList();
        for (Integer deduplicationType : deduplicationList) {
            DeduplicationParam deduplicationParam = deduplicationHolder.selectBuilder(deduplicationType).build(deduplicationConfig, taskInfo);
            if (deduplicationParam != null) {
                deduplicationHolder.selectService(deduplicationType).deduplication(deduplicationParam);
            }
        }
    }


}
