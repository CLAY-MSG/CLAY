package xyz.sgmi.clay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.sgmi.clay.domain.*;
import xyz.sgmi.clay.enums.BusinessCode;
import xyz.sgmi.clay.enums.RespStatusEnum;
import xyz.sgmi.clay.pipeline.BusinessProcess;
import xyz.sgmi.clay.pipeline.ProcessContext;
import xyz.sgmi.clay.pipeline.ProcessController;
import xyz.sgmi.clay.pipeline.ProcessTemplate;
import xyz.sgmi.clay.service.SendServiceImpl;
import xyz.sgmi.clay.vo.BasicResultVO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@ExtendWith(MockitoExtension.class)
class SendServiceImplTest {

    @Spy
    private ProcessController processController;

    @Mock
    private Map<String, ProcessTemplate> templateConfig;

    @Spy
    private ProcessTemplate processTemplate;

    @Mock
    private BusinessProcess businessProcess;

    @InjectMocks
    private SendServiceImpl sendServiceImplUnderTest;

    @Test
    void testSend() {

        // params
        final SendRequest sendRequest = new SendRequest("send", 1L,
                new MessageParam("13711111111", new HashMap<>(), new HashMap<>()));

        // predict result
        final ProcessContext<SendTaskModel> processContext = new ProcessContext<>(sendRequest.getCode(), new SendTaskModel(), false, new BasicResultVO<>(
                RespStatusEnum.SUCCESS, "data"));
        final SendResponse expectedResult = new SendResponse(processContext.getResponse().getStatus(), processContext.getResponse().getMsg());


        // stub
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        processTemplate.setProcessList(Arrays.asList(businessProcess));
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), processTemplate);

        processController.setTemplateConfig(templateConfig);


        // Run the test
        final SendResponse result = sendServiceImplUnderTest.send(sendRequest);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testBatchSend() {
        // Setup
        final BatchSendRequest batchSendRequest = new BatchSendRequest("code", 0L,
                Arrays.asList(new MessageParam("receiver", new HashMap<>(), new HashMap<>())));
        final SendResponse expectedResult = new SendResponse("status", "msg");

        // Configure ProcessController.process(...).
        final ProcessContext processContext = new ProcessContext<>("code", null, false, new BasicResultVO<>(
                RespStatusEnum.SUCCESS, "data"));
        when(processController.process(new ProcessContext<>("code", null, false, new BasicResultVO<>(
                RespStatusEnum.SUCCESS, "data")))).thenReturn(processContext);

        // Run the test
        final SendResponse result = sendServiceImplUnderTest.batchSend(batchSendRequest);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}

