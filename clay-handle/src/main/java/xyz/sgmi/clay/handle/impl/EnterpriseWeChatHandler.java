package xyz.sgmi.clay.handle.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.sgmi.clay.dto.EnterpriseWeChatContentModel;
import xyz.sgmi.clay.enums.ChannelType;
import xyz.sgmi.clay.handle.BaseHandler;
import xyz.sgmi.clay.handle.Handler;
import xyz.sgmi.clay.pojo.TaskInfo;
import xyz.sgmi.clay.utils.AccountUtils;

/**
 * 企业微信推送处理
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Component
@Slf4j
public class EnterpriseWeChatHandler extends BaseHandler implements Handler {

    /**
     * 构建WxCpMessage时需要用的常量
     */
    private static final String ALL = "@all";
    private static final String DELIMITER = "|";

    /**
     * 账号信息
     */
    private static final String ENTERPRISE_WECHAT_ACCOUNT_KEY = "enterpriseWechatAccount";
    private static final String PREFIX = "enterprise_wechat_";

    @Autowired
    private AccountUtils accountUtils;

    public EnterpriseWeChatHandler() {
        channelCode = ChannelType.ENTERPRISE_WE_CHAT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            WxCpDefaultConfigImpl accountConfig = accountUtils.getAccount(taskInfo.getSendAccount(), ENTERPRISE_WECHAT_ACCOUNT_KEY, PREFIX, new WxCpDefaultConfigImpl());
            WxCpMessageServiceImpl messageService = new WxCpMessageServiceImpl(initService(accountConfig));
            WxCpMessageSendResult result = messageService.send(buildWxCpMessage(taskInfo, accountConfig.getAgentId()));
            buildAnchorState(result);
            return true;
        } catch (Exception e) {
            log.error("EnterpriseWeChatHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    /**
     * 打点相关的信息记录
     *
     * @param result
     */
    private void buildAnchorState(WxCpMessageSendResult result) {

    }


    /**
     * 初始化 WxCpServiceImpl 服务接口
     *
     * @param config
     * @return
     */
    private WxCpService initService(WxCpDefaultConfigImpl config) {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        return wxCpService;
    }

    /**
     * 构建企业微信下发消息的对象
     *
     * @param taskInfo
     * @param agentId  应用ID
     * @return
     */
    private WxCpMessage buildWxCpMessage(TaskInfo taskInfo, Integer agentId) {
        String userId;
        if (ALL.equals(CollUtil.getFirst(taskInfo.getReceiver()))) {
            userId = CollUtil.getFirst(taskInfo.getReceiver());
        } else {
            userId = StringUtils.join(taskInfo.getReceiver(), DELIMITER);
        }
        EnterpriseWeChatContentModel enterpriseWeChatContentModel = (EnterpriseWeChatContentModel) taskInfo.getContentModel();
        return WxCpMessage
                .TEXT()
                .agentId(agentId)
                .toUser(userId)
                .content(enterpriseWeChatContentModel.getContent())
                .build();
    }

}

