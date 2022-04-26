package xyz.sgmi.clay.funtion;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import xyz.sgmi.clay.domain.AnchorInfo;

/**
 * process 处理
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public class ClayFlatMapFunction implements FlatMapFunction<String, AnchorInfo> {
    @Override
    public void flatMap(String value, Collector<AnchorInfo> collector) throws Exception {
        AnchorInfo anchorInfo = JSON.parseObject(value, AnchorInfo.class);
        collector.collect(anchorInfo);
    }
}