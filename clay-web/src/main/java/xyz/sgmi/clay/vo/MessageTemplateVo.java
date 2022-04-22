package xyz.sgmi.clay.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.sgmi.clay.domain.MessageTemplate;

/**
 * 消息模板的Vo
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateVo {

    /**
     * 消息模板李彪
     */
    private Iterable<MessageTemplate> rows;

    /**
     * 总条数
     */
    private Long count;
}
