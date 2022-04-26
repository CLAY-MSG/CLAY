package xyz.sgmi.clay.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.dao.MessageTemplateDao;
import xyz.sgmi.clay.domain.CrowdInfoVo;
import xyz.sgmi.clay.domain.MessageTemplate;
import xyz.sgmi.clay.pending.CrowdBatchTaskPending;
import xyz.sgmi.clay.service.TaskHandler;
import xyz.sgmi.clay.utils.ReadFileUtils;

import java.util.HashMap;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Service
@Slf4j
public class TaskHandlerImpl implements TaskHandler {
    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Autowired
    private ApplicationContext context;

    @Override
    @Async
    public void handle(Long messageTemplateId) {
        log.info("TaskHandler handle:{}", Thread.currentThread().getName());

        MessageTemplate messageTemplate = messageTemplateDao.findById(messageTemplateId).get();
        if (messageTemplate == null || StrUtil.isBlank(messageTemplate.getCronCrowdPath())) {
            log.error("TaskHandler#handle crowdPath empty! messageTemplateId:{}", messageTemplateId);
            return;
        }

        CrowdBatchTaskPending crowdBatchTaskPending = context.getBean(CrowdBatchTaskPending.class);
        // 读取文件得到每一行记录给到队列做lazy batch处理
        ReadFileUtils.getCsvRow(messageTemplate.getCronCrowdPath(), row -> {
            if (CollUtil.isEmpty(row.getFieldMap())
                    || StrUtil.isBlank(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))) {
                return;
            }
            HashMap<String, String> params = ReadFileUtils.getParamFromLine(row.getFieldMap());
            CrowdInfoVo crowdInfoVo = CrowdInfoVo.builder().receiver(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))
                    .params(params).messageTemplateId(messageTemplateId).build();
            crowdBatchTaskPending.pending(crowdInfoVo);
        });

    }

}
