package xyz.sgmi.clay.service;

import xyz.sgmi.clay.domain.BatchSendRequest;
import xyz.sgmi.clay.domain.SendRequest;
import xyz.sgmi.clay.domain.SendResponse;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public interface SendService {


    /**
     * 单文案发送接口
     *
     * @param sendRequest
     * @return
     */
    SendResponse send(SendRequest sendRequest);


    /**
     * 多文案发送接口
     *
     * @param batchSendRequest
     * @return
     */
    SendResponse batchSend(BatchSendRequest batchSendRequest);
}
