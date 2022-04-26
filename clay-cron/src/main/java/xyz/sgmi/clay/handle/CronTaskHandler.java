package xyz.sgmi.clay.handle;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.service.TaskHandler;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Service
@Slf4j
public class CronTaskHandler {
    @Autowired
    private TaskHandler taskHandler;
    /**
     * 处理所有的 austin 定时任务消息
     */
    @XxlJob("austinJobHandler")
    public void execute() {
        log.info("CronTaskHandler#execute messageTemplateId:{} cron exec!", XxlJobHelper.getJobParam());
        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());

        taskHandler.handle(messageTemplateId);

    }

}