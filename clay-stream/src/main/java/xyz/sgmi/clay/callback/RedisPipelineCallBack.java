package xyz.sgmi.clay.callback;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.List;

/**
 * redis pipeline接口定义
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public interface RedisPipelineCallBack {

    /**
     * 具体执行逻辑
     *
     * @param redisAsyncCommands
     * @return
     */
    List<RedisFuture<?>> invoke(RedisAsyncCommands redisAsyncCommands);

}