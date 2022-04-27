package xyz.sgmi.clay.handle.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.google.common.base.Throwables;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.sgmi.clay.constant.SendAccountConstant;
import xyz.sgmi.clay.dto.model.EmailContentModel;
import xyz.sgmi.clay.enums.ChannelType;
import xyz.sgmi.clay.handle.BaseHandler;
import xyz.sgmi.clay.handle.Handler;
import xyz.sgmi.clay.pojo.TaskInfo;
import xyz.sgmi.clay.utils.AccountUtils;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Component
@Slf4j
public class EmailHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccountConfig(taskInfo.getSendAccount());
        try {
            MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle(),
                    emailContentModel.getContent(), true, null);
        } catch (Exception e) {
            log.error("EmailHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }
        return true;
    }

    /**
     * 获取账号信息合配置
     *
     * @return
     */
    private MailAccount getAccountConfig(Integer sendAccount) {
        MailAccount account = accountUtils.getAccount(sendAccount, SendAccountConstant.EMAIL_ACCOUNT_KEY, SendAccountConstant.EMAIL_ACCOUNT_PREFIX, new MailAccount());
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setAuth(true).setStarttlsEnable(true).setSslEnable(true).setCustomProperty("mail.smtp.ssl.socketFactory", sf);
            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (Exception e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }

}
