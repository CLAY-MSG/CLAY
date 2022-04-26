package xyz.sgmi.clay.handle;


import xyz.sgmi.clay.pojo.TaskInfo;

/**
 * 消息处理器
 * @author MSG
 */
public interface Handler {

    /**
     * 处理器
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

}
