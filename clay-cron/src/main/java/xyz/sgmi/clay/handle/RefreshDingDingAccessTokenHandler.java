package xyz.sgmi.clay.handle;

import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.google.common.base.Throwables;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.sgmi.clay.config.SupportThreadPoolConfig;
import xyz.sgmi.clay.constant.ClayConstant;
import xyz.sgmi.clay.constant.SendAccountConstant;
import xyz.sgmi.clay.dto.account.DingDingWorkNoticeAccount;
import xyz.sgmi.clay.utils.AccountUtils;

/**
 * 刷新钉钉的access_token
 * <p>
 * https://open.dingtalk.com/document/orgapp-server/obtain-orgapp-token
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Service
@Slf4j
public class RefreshDingDingAccessTokenHandler {


    private static final String URL = "https://oapi.dingtalk.com/gettoken";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AccountUtils accountUtils;

    /**
     * 每小时请求一次接口刷新（以防失效)
     */
    @XxlJob("refreshAccessTokenJob")
    public void execute() {
        log.info("refreshAccessTokenJob#execute!");
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            for (int index = SendAccountConstant.START; true; index = index + SendAccountConstant.STEP) {
                DingDingWorkNoticeAccount account = accountUtils.getAccount(index, SendAccountConstant.DING_DING_WORK_NOTICE_ACCOUNT_KEY, SendAccountConstant.DING_DING_WORK_NOTICE_PREFIX, new DingDingWorkNoticeAccount());
                if (account == null) {
                    break;
                }
                String accessToken = getAccessToken(account);
                if (StrUtil.isNotBlank(accessToken)) {
                    redisTemplate.opsForValue().set(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + index, accessToken);
                }
            }
        });
    }

    /**
     * 获取 access_token
     *
     * @param account
     * @return
     */
    private String getAccessToken(DingDingWorkNoticeAccount account) {
        String accessToken = "";
        try {
            DingTalkClient client = new DefaultDingTalkClient(URL);
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey(account.getAppKey());
            req.setAppsecret(account.getAppSecret());
            req.setHttpMethod(ClayConstant.REQUEST_METHOD_GET);
            OapiGettokenResponse rsp = client.execute(req);
            accessToken = rsp.getAccessToken();
        } catch (Exception e) {
            log.error("RefreshDingDingAccessTokenHandler#getAccessToken fail:{}", Throwables.getStackTraceAsString(e));
        }
        return accessToken;
    }
}
