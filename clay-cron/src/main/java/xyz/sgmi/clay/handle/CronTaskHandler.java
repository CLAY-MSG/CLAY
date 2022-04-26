package xyz.sgmi.clay.handle;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.domain.MessageTemplate;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Service
@Slf4j
public class CronTaskHandler {

    /**
     * 处理所有的 austin 定时任务消息
     */
    @XxlJob("austinJobHandler")
    public void execute() {
        log.info("XXL-JOB, Hello World.");
        MessageTemplate messageTemplate = JSON.parseObject(XxlJobHelper.getJobParam(), MessageTemplate.class);
    }

}