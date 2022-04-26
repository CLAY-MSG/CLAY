package xyz.sgmi.clay.enums;

/**
 * 调度过期策略
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
public enum MisfireStrategyEnum {

    /**
     * do nothing
     */
    DO_NOTHING,

    /**
     * fire once now
     */
    FIRE_ONCE_NOW;

    MisfireStrategyEnum() {
    }
}
