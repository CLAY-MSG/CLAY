package xyz.sgmi.clay.pipeline;

/**
 * 业务执行器
 *
 * @author MSG
 */
public interface BusinessProcess<T extends ProcessModel> {

    /**
     * 真正处理逻辑
     * @param context
     */
    void process(ProcessContext<T> context);
}
