package xyz.sgmi.clay;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import xyz.sgmi.clay.constants.ClayFlinkConstant;
import xyz.sgmi.clay.domain.AnchorInfo;
import xyz.sgmi.clay.funtion.ClayFlatMapFunction;
import xyz.sgmi.clay.sink.ClaySink;
import xyz.sgmi.clay.utils.MessageQueueUtils;

/**
 * flink启动类
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Slf4j
public class ClayBootStrap {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        /**
         * 1.获取KafkaConsumer
         */
        KafkaSource<String> kafkaConsumer = MessageQueueUtils.getKafkaConsumer(ClayFlinkConstant.TOPIC_NAME, ClayFlinkConstant.GROUP_ID, ClayFlinkConstant.BROKER);
        DataStreamSource<String> kafkaSource = env.fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), ClayFlinkConstant.SOURCE_NAME);


        /**
         * 2. 数据转换处理
         */
        SingleOutputStreamOperator<AnchorInfo> dataStream = kafkaSource.flatMap(new ClayFlatMapFunction()).name(ClayFlinkConstant.FUNCTION_NAME);

        /**
         * 3. 将实时数据多维度写入Redis(已实现)，离线数据写入hive(未实现)
         */
        dataStream.addSink(new ClaySink()).name(ClayFlinkConstant.SINK_NAME);
        env.execute(ClayFlinkConstant.JOB_NAME);

    }

}
