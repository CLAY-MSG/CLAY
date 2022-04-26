package xyz.sgmi.clay.handle;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.sgmi.clay.domain.AnchorInfo;
import xyz.sgmi.clay.enums.AnchorState;
import xyz.sgmi.clay.pojo.TaskInfo;
import xyz.sgmi.clay.utils.LogUtils;

import javax.annotation.PostConstruct;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public abstract class BaseHandler implements Handler {

    /**
     * 标识渠道的Code
     * 子类初始化的时候指定
     */
    protected Integer channelCode;


    @Autowired
    private HandlerHolder handlerHolder;

    /**
     * 初始化渠道与Handler的映射关系
     */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    @Override
    public void doHandler(TaskInfo taskInfo) {
        if (handler(taskInfo)) {
            LogUtils.print(AnchorInfo.builder().state(AnchorState.SEND_SUCCESS.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
            return;
        }
        LogUtils.print(AnchorInfo.builder().state(AnchorState.SEND_FAIL.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
    }

    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     * @return
     */
    public abstract boolean handler(TaskInfo taskInfo);

}
