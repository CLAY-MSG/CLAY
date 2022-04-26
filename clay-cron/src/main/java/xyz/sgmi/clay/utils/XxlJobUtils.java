package xyz.sgmi.clay.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.sgmi.clay.constant.ClayConstant;
import xyz.sgmi.clay.constants.XxlJobConstant;
import xyz.sgmi.clay.domain.MessageTemplate;
import xyz.sgmi.clay.entity.XxlJobGroup;
import xyz.sgmi.clay.entity.XxlJobInfo;
import xyz.sgmi.clay.enums.*;
import xyz.sgmi.clay.service.CronTaskService;
import xyz.sgmi.clay.vo.BasicResultVO;

import java.util.Date;

/**
 * xxlJob工具类
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Component
public class XxlJobUtils {

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.jobHandlerName}")
    private String jobHandlerName;

    @Autowired
    private CronTaskService cronTaskService;



    public XxlJobInfo buildXxlJobInfo(MessageTemplate messageTemplate) {

        String scheduleConf = messageTemplate.getExpectPushTime();
        // 如果没有指定cron表达式，说明立即执行(给到xxl-job延迟5秒的cron表达式)
        if (messageTemplate.getExpectPushTime().equals(String.valueOf(ClayConstant.FALSE))) {
            scheduleConf = DateUtil.format(DateUtil.offsetSecond(new Date(), XxlJobConstant.DELAY_TIME), ClayConstant.CRON_FORMAT);
        }

        XxlJobInfo xxlJobInfo = XxlJobInfo.builder()
                .jobGroup(queryJobGroupId()).jobDesc(messageTemplate.getName())
                .author(messageTemplate.getCreator())
                .scheduleConf(scheduleConf)
                .scheduleType(ScheduleTypeEnum.CRON.name())
                .misfireStrategy(MisfireStrategyEnum.DO_NOTHING.name())
                .executorRouteStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
                .executorHandler(XxlJobConstant.JOB_HANDLER_NAME)
                .executorParam(String.valueOf(messageTemplate.getId()))
                .executorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name())
                .executorTimeout(XxlJobConstant.TIME_OUT)
                .executorFailRetryCount(XxlJobConstant.RETRY_COUNT)
                .glueType(GlueTypeEnum.BEAN.name())
                .triggerStatus(ClayConstant.FALSE)
                .glueRemark(StrUtil.EMPTY)
                .glueSource(StrUtil.EMPTY)
                .alarmEmail(StrUtil.EMPTY)
                .childJobId(StrUtil.EMPTY).build();

        if (messageTemplate.getCronTaskId() != null) {
            xxlJobInfo.setId(messageTemplate.getCronTaskId());
        }
        return xxlJobInfo;
    }

    /**
     * 根据就配置文件的内容获取jobGroupId，没有则创建
     * @return
     */
    private Integer queryJobGroupId() {
        BasicResultVO basicResultVO = cronTaskService.getGroupId(appName, jobHandlerName);
        if (basicResultVO.getData() == null) {
            XxlJobGroup xxlJobGroup = XxlJobGroup.builder().appname(appName).title(jobHandlerName).addressType(ClayConstant.FALSE).build();
            if (RespStatusEnum.SUCCESS.getCode().equals(cronTaskService.createGroup(xxlJobGroup).getStatus())) {
                return (int) cronTaskService.getGroupId(appName, jobHandlerName).getData();
            }
        }
        return (Integer) basicResultVO.getData();
    }

}
