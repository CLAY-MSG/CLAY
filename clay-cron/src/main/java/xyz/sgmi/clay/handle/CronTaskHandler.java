package xyz.sgmi.clay.handle;

import com.dtp.core.thread.DtpExecutor;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.config.CronAsyncThreadPoolConfig;
import xyz.sgmi.clay.service.TaskHandler;
import xyz.sgmi.clay.utils.ThreadPoolUtils;

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

    @Autowired
    private ThreadPoolUtils threadPoolUtils;
    private DtpExecutor dtpExecutor = CronAsyncThreadPoolConfig.getXxlCronExecutor();

    /**
     * 处理所有的 austin 定时任务消息
     */
    @XxlJob("austinJob")
    public void execute() {
        log.info("CronTaskHandler#execute messageTemplateId:{} cron exec!", XxlJobHelper.getJobParam());
        threadPoolUtils.register(dtpExecutor);

        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());
        dtpExecutor.execute(() -> taskHandler.handle(messageTemplateId));

    }

}
