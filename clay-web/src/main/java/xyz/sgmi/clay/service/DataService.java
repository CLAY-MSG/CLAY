package xyz.sgmi.clay.service;

import xyz.sgmi.clay.vo.amis.EchartsVo;
import xyz.sgmi.clay.vo.amis.UserTimeLineVo;

/**
 * 数据链路追踪获取接口
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public interface DataService {

    /**
     * 获取全链路追踪 用户维度信息
     *
     * @param receiver 接收者
     * @return
     */
    UserTimeLineVo getTraceUserInfo(String receiver);


    /**
     * 获取全链路追踪 消息模板维度信息
     *
     * @param businessId 业务ID（如果传入消息模板ID，则生成当天的业务ID）
     * @return
     */
    EchartsVo getTraceMessageTemplateInfo(String businessId);


}
