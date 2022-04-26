package xyz.sgmi.clay.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.sgmi.clay.utils.RedisUtils;
import xyz.sgmi.clay.vo.BasicResultVO;
import xyz.sgmi.clay.vo.DataParam;

import java.util.Map;

/**
 * 获取数据接口（全链路追踪)
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/trace")
@Api("获取数据接口（全链路追踪)")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
public class DataController {

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/data")
    @ApiOperation("/获取数据")
    public BasicResultVO getData(@RequestBody DataParam dataParam) {


        Long businessId = dataParam.getBusinessId();
        Map<Object, Object> objectObjectMap = redisUtils.hGetAll(String.valueOf(businessId));
        log.info("data:{}", JSON.toJSONString(objectObjectMap));
        return BasicResultVO.success();
    }

}
