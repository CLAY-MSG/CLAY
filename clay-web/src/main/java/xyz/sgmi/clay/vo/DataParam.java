package xyz.sgmi.clay.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全链路 请求参数
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataParam {
    /**
     * 传入userId查看用户的链路信息
     */
    private String receiver;


    /**
     * 业务Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     * 如果传入的是模板ID，则生成当天的业务ID
     */
    private String businessId;


}
