package xyz.sgmi.clay.domain.sms;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @author MSG
 */
@Data
@Builder
public class SmsParam {

    /**
     * 业务Id
     */
    private Long messageTemplateId;

    /**
     * 需要发送的手机号
     */
    private Set<String> phones;

    /**
     * 发送文案
     */
    private String content;

    /**
     * 发送账号
     */
    private Integer sendAccount;

}
