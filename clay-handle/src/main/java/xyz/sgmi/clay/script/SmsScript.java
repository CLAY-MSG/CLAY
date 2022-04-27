package xyz.sgmi.clay.script;

import xyz.sgmi.clay.domain.SmsRecord;
import xyz.sgmi.clay.domain.sms.SmsParam;

import java.util.List;

/**
 * 短信脚本 接口
 * @author MSG
 */
public interface SmsScript {


    /**
     * 发送短信
     * @param smsParam 发送短信参数
     * @return 渠道商接口返回值
     */
    List<SmsRecord> send(SmsParam smsParam) throws Exception;

}

