package xyz.sgmi.clay.service.discard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.constant.ClayConstant;
import xyz.sgmi.clay.domain.AnchorInfo;
import xyz.sgmi.clay.enums.AnchorState;
import xyz.sgmi.clay.pojo.TaskInfo;
import xyz.sgmi.clay.utils.LogUtils;

/**
 * 丢弃模板消息
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Service
public class DiscardMessageService {
    private static final String DISCARD_MESSAGE_KEY = "discard";

    @ApolloConfig("boss.clay")
    private Config config;

    /**
     * 丢弃消息，配置在apollo
     * @param taskInfo
     * @return
     */
    public boolean isDiscard(TaskInfo taskInfo) {
        JSONArray array = JSON.parseArray(config.getProperty(DISCARD_MESSAGE_KEY,
                ClayConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY));
        if (array.contains(String.valueOf(taskInfo.getMessageTemplateId()))) {
            LogUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).state(AnchorState.DISCARD.getCode()).build());
            return true;
        }
        return false;
    }

}
