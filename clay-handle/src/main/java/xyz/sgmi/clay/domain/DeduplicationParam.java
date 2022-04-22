package xyz.sgmi.clay.domain;

import lombok.Builder;
import lombok.Data;
import xyz.sgmi.clay.enums.AnchorState;
import xyz.sgmi.clay.pojo.TaskInfo;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 * 去重服务所需要的参数
 */
@Builder
@Data
public class DeduplicationParam {

    /**
     * TaskIno信息
     */
    private TaskInfo taskInfo;

    /**
     * 去重时间
     * 单位：秒
     */
    private Long deduplicationTime;

    /**
     * 需达到的次数去重
     */
    private Integer countNum;

    /**
     * 标识属于哪种去重
     */
    private AnchorState anchorState;
}
