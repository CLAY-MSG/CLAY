package xyz.sgmi.clay.service.deduplication.build;

import xyz.sgmi.clay.domain.DeduplicationParam;
import xyz.sgmi.clay.pojo.TaskInfo;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public interface Builder {

    String DEDUPLICATION_CONFIG_PRE = "deduplication_";

    /**
     * 根据配置构建去重参数
     *
     * @param deduplication
     * @param taskInfo
     * @return
     */
    DeduplicationParam build(String deduplication, TaskInfo taskInfo);
}
