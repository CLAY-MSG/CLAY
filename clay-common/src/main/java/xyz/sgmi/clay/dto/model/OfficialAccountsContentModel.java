package xyz.sgmi.clay.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficialAccountsContentModel extends ContentModel {

    /**
     * 模板消息发送的数据
     */
    Map<String, String> map;

    /**
     * 模板消息跳转的url
     */
    String url;

}
