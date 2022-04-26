package xyz.sgmi.clay.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息模板管理 请求参数
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateParam {

    /**
     * 当前页码
     */
    private Integer page ;

    /**
     * 当前页大小
     */
    private Integer perPage;

    /**
     * 模板ID
     */
    private Long id;

    /**
     * 消息接收者(测试发送时使用)
     */
    private String receiver;

    /**
     * 下发参数信息
     */
    private String msgContent;

}

