package xyz.sgmi.clay.handle.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.dto.account.DingDingWorkNoticeAccount;
import xyz.sgmi.clay.enums.ChannelType;
import xyz.sgmi.clay.handle.BaseHandler;
import xyz.sgmi.clay.pojo.TaskInfo;
import xyz.sgmi.clay.utils.AccountUtils;

/**
 * 钉钉消息自定义机器人 消息处理器
 * <p>
 * https://open.dingtalk.com/document/group/custom-robot-access
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Slf4j
@Service
public class DingDingWorkNoticeHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;


    public DingDingWorkNoticeHandler() {
        channelCode = ChannelType.DING_DING_WORK_NOTICE.getCode();
    }

    private static final String URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";


    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            DingDingWorkNoticeAccount account = accountUtils.getAccount(taskInfo.getSendAccount(), SendAccountConstant.DING_DING_WORK_NOTICE_ACCOUNT_KEY, SendAccountConstant.DING_DING_WORK_NOTICE_PREFIX, new DingDingWorkNoticeAccount());
            OapiMessageCorpconversationAsyncsendV2Request request = assembleParam(account, taskInfo);
            String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + taskInfo.getSendAccount());
            OapiMessageCorpconversationAsyncsendV2Response response = new DefaultDingTalkClient(URL).execute(request, accessToken);
            if (response.getErrcode() == 0) {
                return true;
            }
            // 常见的错误 应当 关联至 AnchorState,由austin后台统一透出失败原因
            log.error("DingDingWorkNoticeHandler#handler fail!result:{},params:{}", JSON.toJSONString(response), JSON.toJSONString(taskInfo));
        } catch (Exception e) {
            log.error("DingDingWorkNoticeHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
        }
        return false;
    }

    /**
     * 拼装参数
     *
     * @param account
     * @param taskInfo
     */
    private OapiMessageCorpconversationAsyncsendV2Request assembleParam(DingDingWorkNoticeAccount account, TaskInfo taskInfo) {
        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        DingDingContentModel contentModel = (DingDingContentModel) taskInfo.getContentModel();
        try {
            // 接收者相关
            if (AustinConstant.SEND_ALL.equals(CollUtil.getFirst(taskInfo.getReceiver()))) {
                req.setToAllUser(true);
            } else {
                req.setUseridList(StringUtils.join(taskInfo.getReceiver(), StrUtil.C_COMMA));
            }
            req.setAgentId(Long.parseLong(account.getAgentId()));

            // 内容相关
            OapiMessageCorpconversationAsyncsendV2Request.Msg message = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            message.setMsgtype("text");
            OapiMessageCorpconversationAsyncsendV2Request.Text textObj = new OapiMessageCorpconversationAsyncsendV2Request.Text();
            textObj.setContent(contentModel.getContent());
            message.setText(textObj);

            req.setMsg(message);
        } catch (Exception e) {
            log.error("assembleParam fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return req;
    }
}

