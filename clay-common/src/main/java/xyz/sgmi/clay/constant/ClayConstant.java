package xyz.sgmi.clay.constant;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 * 基础的常量信息
 */
public class ClayConstant {

    /**
     * boolean转换
     */
    public final static Integer TRUE = 1;
    public final static Integer FALSE = 0;

    /**
     * cron时间格式
     */
    public final static String CRON_FORMAT = "ss mm HH dd MM ? yyyy-yyyy";

    /**
     * apollo默认的值
     */
    public final static String APOLLO_DEFAULT_VALUE_JSON_OBJECT = "{}";
    public final static String APOLLO_DEFAULT_VALUE_JSON_ARRAY = "[]";
    /**
     * businessId默认的长度
     * 生成的逻辑：com.java3y.austin.support.utils.TaskInfoUtils#generateBusinessId(java.lang.Long, java.lang.Integer)
     */
    public final static Integer BUSINESS_ID_LENGTH = 16;





}
