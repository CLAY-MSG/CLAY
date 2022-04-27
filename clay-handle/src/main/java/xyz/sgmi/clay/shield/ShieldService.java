package xyz.sgmi.clay.shield;

import xyz.sgmi.clay.pojo.TaskInfo;

/**
 * 屏蔽服务
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public interface ShieldService {

    /**
     * 屏蔽消息
     * @param taskInfo
     */
    void shield(TaskInfo taskInfo);
}

