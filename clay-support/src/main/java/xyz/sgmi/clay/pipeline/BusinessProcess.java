package xyz.sgmi.clay.pipeline;

/**
 * 业务执行器
 *
 * @author MSG
 */
public interface BusinessProcess {

    /**
     * 真正处理逻辑
     * @param context
     */
    void process(ProcessContext context);
}