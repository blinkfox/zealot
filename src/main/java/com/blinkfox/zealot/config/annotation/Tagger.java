package com.blinkfox.zealot.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于配置Zealot xml标签和对应Handler的注解,其会标注在Zealot的Handler类中.
 *
 * @author blinkfox on 2018-04-26.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tagger {

    /**
     * xml标签的名称，如: 'in', 'equal'等.
     *
     * @return 字符串值
     */
    String value();

    /**
     * 生成动态SQL时添加的前缀，如: ' AND ', ' OR '等.
     *
     * @return 字符串值
     */
    String prefix() default "";

    /**
     * SQL操作符，如: ' = ? ', ' LIKE ? ', ' IN '等.
     *
     * @return 字符串值
     */
    String symbol() default "";

}