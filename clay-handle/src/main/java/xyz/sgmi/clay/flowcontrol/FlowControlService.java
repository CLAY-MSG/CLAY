package xyz.sgmi.clay.flowcontrol;

import xyz.sgmi.clay.pojo.TaskInfo;

/**
 * 流量控制服务
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public interface FlowControlService {


    /**
     * 根据渠道进行流量控制
     *
     * @param taskInfo
     * @param flowControlParam
     */
    void flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam);

}

