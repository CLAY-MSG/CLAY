package xyz.sgmi.clay.service.deduplication.build;

import cn.hutool.core.date.DateUtil;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.domain.DeduplicationParam;
import xyz.sgmi.clay.enums.AnchorState;
import xyz.sgmi.clay.enums.DeduplicationType;
import xyz.sgmi.clay.pojo.TaskInfo;

import java.util.Date;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Service
public class FrequencyDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {
    public FrequencyDeduplicationBuilder() {
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicationParam.setAnchorState(AnchorState.RULE_DEDUPLICATION);
        return deduplicationParam;
    }
}
