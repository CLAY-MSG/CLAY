package xyz.sgmi.clay.service;

import xyz.sgmi.clay.domain.MessageTemplate;
import xyz.sgmi.clay.vo.BasicResultVO;
import xyz.sgmi.clay.vo.MessageTemplateParam;

import java.util.List;

/**
 * 消息模板管理 接口
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public interface MessageTemplateService {


    /**
     * 查询未删除的模板列表（分页)
     *
     * @param messageTemplateParam
     * @return
     */
    List<MessageTemplate> queryList(MessageTemplateParam messageTemplateParam);

    /**
     * 统计未删除的条数
     *
     * @return
     */
    Long count();

    /**
     * 单个 保存或者更新
     * 存在ID 更新
     * 不存在ID保存
     *
     * @param messageTemplate
     * @return
     */
    MessageTemplate saveOrUpdate(MessageTemplate messageTemplate);


    /**
     * 软删除(deleted=1)
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据ID查询模板信息
     *
     * @param id
     * @return
     */
    MessageTemplate queryById(Long id);

    /**
     * 复制配置
     *
     * @param id
     */
    void copy(Long id);


    /**
     * 启动模板的定时任务
     */
    BasicResultVO startCronTask(Long id);

    /**
     * 暂停模板的定时任务
     */
    BasicResultVO stopCronTask(Long id);


}
