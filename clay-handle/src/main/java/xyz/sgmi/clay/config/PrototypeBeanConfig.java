package xyz.sgmi.clay.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import xyz.sgmi.clay.pending.Task;
import xyz.sgmi.clay.receiver.Receiver;

/**
 * Handler模块的配置信息
 *
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Configuration
public class PrototypeBeanConfig {

    /**
     * 定义多例的Receiver
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Receiver receiver() {
        return new Receiver();
    }

    /**
     * 定义多例的Task
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Task task() {
        return new Task();
    }

}