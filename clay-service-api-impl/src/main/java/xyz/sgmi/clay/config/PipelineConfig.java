package xyz.sgmi.clay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.sgmi.clay.action.AfterParamCheckAction;
import xyz.sgmi.clay.action.AssembleAction;
import xyz.sgmi.clay.action.PreParamCheckAction;
import xyz.sgmi.clay.action.SendMqAction;
import xyz.sgmi.clay.enums.BusinessCode;
import xyz.sgmi.clay.pipeline.ProcessController;
import xyz.sgmi.clay.pipeline.ProcessTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * api层的pipeline配置类
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Configuration
public class PipelineConfig {

    @Autowired
    private PreParamCheckAction preParamCheckAction;
    @Autowired
    private AssembleAction assembleAction;
    @Autowired
    private AfterParamCheckAction afterParamCheckAction;
    @Autowired
    private SendMqAction sendMqAction;

    /**
     * 普通发送执行流程
     * 1. 前置参数校验
     * 2. 组装参数
     * 3. 后置参数校验
     * 4. 发送消息至MQ
     * @return
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList(preParamCheckAction, assembleAction,
                afterParamCheckAction, sendMqAction));
        return processTemplate;
    }

    /**
     * pipeline流程控制器
     * 目前暂定只有 普通发送的流程
     * 后续扩展则加BusinessCode和ProcessTemplate
     *
     * @return
     */
    @Bean
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), commonSendTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }

}
