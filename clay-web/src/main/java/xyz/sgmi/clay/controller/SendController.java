package xyz.sgmi.clay.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.sgmi.clay.domain.SendRequest;
import xyz.sgmi.clay.domain.SendResponse;
import xyz.sgmi.clay.service.SendService;

/**
 * @author MSG
 */
@Api(tags={"发送消息"})
@RestController
public class SendController {

    @Autowired
    private SendService sendService;


    /**
     * 发送消息接口
     *
     * @return
     */
    @ApiOperation(value = "下发接口",notes = "多渠道多类型下发消息，目前支持邮件和短信，类型支持：验证码、通知类、营销类")
    @PostMapping("/send")
    public SendResponse send(@ApiParam(value = "下发消息参数",required = true, examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\"code\":\"send\",\"messageParam\":{\"receiver\":\"13788888888\",\"variables\":{\"title\":\"yyyyyy\",\"contentValue\":\"6666164180\"}},\"messageTemplateId\":1}")),example = "{\"code\":\"send\",\"messageParam\":{\"receiver\":\"13788888888\",\"variables\":{\"title\":\"yyyyyy\",\"contentValue\":\"6666164180\"}},\"messageTemplateId\":1}")
                             @RequestBody SendRequest sendRequest) {
        return sendService.send(sendRequest);
    }
}
