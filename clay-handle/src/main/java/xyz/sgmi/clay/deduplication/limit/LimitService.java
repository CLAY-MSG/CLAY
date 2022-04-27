package xyz.sgmi.clay.deduplication.limit;

import xyz.sgmi.clay.deduplication.DeduplicationParam;
import xyz.sgmi.clay.pojo.TaskInfo;
import xyz.sgmi.clay.service.deduplication.service.AbstractDeduplicationService;

import java.util.Set;

/**
 *去重限制
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public interface LimitService {


    /**
     * @param service 去重器对象
     * @param taskInfo
     * @param param 去重参数
     * @return 返回不符合条件的手机号码
     */
    Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param);

}

