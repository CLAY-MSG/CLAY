package xyz.sgmi.clay.enums;

/**
 * 调度类型
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public enum ScheduleTypeEnum {

    NONE,
    /**
     * schedule by cron
     */
    CRON,

    /**
     * schedule by fixed rate (in seconds)
     */
    FIX_RATE;

    ScheduleTypeEnum() {
    }

}

