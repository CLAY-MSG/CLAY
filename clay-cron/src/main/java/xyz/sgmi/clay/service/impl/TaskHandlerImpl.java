package xyz.sgmi.clay.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.csv.CountFileRowHandler;
import xyz.sgmi.clay.dao.MessageTemplateDao;
import xyz.sgmi.clay.domain.CrowdInfoVo;
import xyz.sgmi.clay.domain.MessageTemplate;
import xyz.sgmi.clay.pending.AbstractLazyPending;
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
    public void handle(Long messageTemplateId) {

        MessageTemplate messageTemplate = messageTemplateDao.findById(messageTemplateId).get();
        if (messageTemplate == null || StrUtil.isBlank(messageTemplate.getCronCrowdPath())) {
            log.error("TaskHandler#handle crowdPath empty! messageTemplateId:{}", messageTemplateId);
            return;
        }

        // 1. 获取文件行数大小
        long countCsvRow = ReadFileUtils.countCsvRow(messageTemplate.getCronCrowdPath(), new CountFileRowHandler());

        // 2. 读取文件得到每一行记录给到队列做lazy batch处理
        CrowdBatchTaskPending crowdBatchTaskPending = context.getBean(CrowdBatchTaskPending.class);
        ReadFileUtils.getCsvRow(messageTemplate.getCronCrowdPath(), row -> {
            if (CollUtil.isEmpty(row.getFieldMap())
                    || StrUtil.isBlank(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))) {
                return;
            }

            // 3. 每一行处理交给LazyPending
            HashMap<String, String> params = ReadFileUtils.getParamFromLine(row.getFieldMap());
            CrowdInfoVo crowdInfoVo = CrowdInfoVo.builder().receiver(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))
                    .params(params).messageTemplateId(messageTemplateId).build();
            crowdBatchTaskPending.pending(crowdInfoVo);

            // 4. 判断是否读取文件完成回收资源且更改状态
            onComplete(row, countCsvRow, crowdBatchTaskPending, messageTemplateId);
        });
    }

    /**
     * 文件遍历结束时
     * 1. 暂停单线程池消费(最后会回收线程池资源)
     * 2. 更改消息模板的状态(暂未实现)
     *
     * @param row
     * @param countCsvRow
     * @param crowdBatchTaskPending
     * @param messageTemplateId
     */
    private void onComplete(CsvRow row, long countCsvRow, AbstractLazyPending crowdBatchTaskPending, Long messageTemplateId) {
        if (row.getOriginalLineNumber() == countCsvRow) {
            crowdBatchTaskPending.setStop(true);
            log.info("messageTemplate:[{}] read csv file complete!", messageTemplateId);
        }
    }
}
