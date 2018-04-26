package com.blinkfox.zealot.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Zealot XML标签注解Tagger的数组，由于Java8以前不支持重复注解，为了更好的支持Java6、Java7，设置此注解.
 *
 * @author blinkfox on 2018/4/26.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Taggers {

    /**
     * Tagger注解的数组.
     *
     * @return 数组
     */
    Tagger[] value();

}
