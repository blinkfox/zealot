package com.blinkfox.zealot.config.entity;

/**
 * debug模式对应的debug枚举类型.
 * Created by blinkfox on 2017/07/24.
 */
public enum DebugType {

    /**
     * 针对单个XML文件内的debug类型.
     * <p>好处是：效率高；坏处是:如果新增了zealot的XML文件必须重启servlet容器.</p>
     */
    SINGLE_FILE,

    /**
     * 针对所有XML文件的debug类型.
     * <p>好处是：如果新增了zealot的XML文件，不需要重启servlet容器；坏处是效率较低.</p>
     */
    ALL_FILE;

}