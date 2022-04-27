package xyz.sgmi.clay.service.deduplication.build;

import org.springframework.stereotype.Service;
import xyz.sgmi.clay.domain.DeduplicationParam;
import xyz.sgmi.clay.enums.AnchorState;
import xyz.sgmi.clay.enums.DeduplicationType;
import xyz.sgmi.clay.pojo.TaskInfo;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {

    public ContentDeduplicationBuilder() {
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);

        if (deduplicationParam == null) {
            return null;
        }

        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);
        return deduplicationParam;

    }
}
