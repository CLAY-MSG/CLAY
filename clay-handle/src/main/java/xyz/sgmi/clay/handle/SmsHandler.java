package xyz.sgmi.clay.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.sgmi.clay.dao.SmsRecordDao;
import xyz.sgmi.clay.domain.SmsRecord;
import xyz.sgmi.clay.dto.SmsContentModel;
import xyz.sgmi.clay.enums.ChannelType;
import xyz.sgmi.clay.pojo.SmsParam;
import xyz.sgmi.clay.pojo.TaskInfo;
import xyz.sgmi.clay.script.SmsScript;

import java.util.List;

/**
 * 短信发送处理
 * @author MSG
 */
@Component
@Slf4j
public class SmsHandler extends Handler {
    @Autowired
    private SmsRecordDao smsRecordDao;

    @Autowired
    private SmsScript smsScript;

    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {

        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .build();
        try {
            List<SmsRecord> recordList = smsScript.send(smsParam);
            if (!CollUtil.isEmpty(recordList)) {
                smsRecordDao.saveAll(recordList);
            }
            return true;
        } catch (Exception e) {
            log.error("SmsHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
        }
        return false;
    }


    /**
     * 如果有输入链接，则把链接拼在文案后
     * <p>
     * PS: 这里可以考虑将链接 转 短链
     * PS: 如果是营销类的短信，需考虑拼接 回TD退订 之类的文案
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            return smsContentModel.getContent() + " " + smsContentModel.getUrl();
        } else {
            return smsContentModel.getContent();
        }
    }
}

