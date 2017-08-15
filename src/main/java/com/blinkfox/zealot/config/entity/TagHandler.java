package com.blinkfox.zealot.config.entity;

import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.IConditHandler;

/**
 * 标签对应的动态sql生成的处理类.
 * Created by blinkfox on 2016-11-01.
 */
public class TagHandler {

    /** 生成sql的前缀,如:and, or 等. */
    private String prefix;

    /** 生成动态sql的处理实现反射类型，如:EqualHandler. */
    private Class<? extends IConditHandler> handlerCls;

    /** 生成sql的后缀,如:>, <, = 等. */
    private String suffix;

    /**
     * 仅标签对应处理类的构造方法.
     * @param handlerCls 动态处理类的反射类型
     */
    public TagHandler(Class<? extends IConditHandler> handlerCls) {
        this.prefix = ZealotConst.ONE_SPACE;
        this.handlerCls = handlerCls;
    }

    /**
     * 含前缀、标签处理器的构造方法.
     * @param prefix sql前缀
     * @param handlerCls 动态处理类的反射类型
     */
    public TagHandler(String prefix, Class<? extends IConditHandler> handlerCls) {
        this.prefix = prefix;
        this.handlerCls = handlerCls;
    }

    /**
     * 含标签处理器、后缀的构造方法.
     * @param handlerCls 动态处理类的反射类型
     * @param suffix sql前后缀
     */
    public TagHandler(Class<? extends IConditHandler> handlerCls, String suffix) {
        this.prefix = ZealotConst.ONE_SPACE;
        this.handlerCls = handlerCls;
        this.suffix = suffix;
    }

    /**
     * 全构造方法.
     * @param prefix sql前缀
     * @param suffix sql后缀
     * @param handlerCls 动态处理类的反射类型
     */
    public TagHandler(String prefix, Class<? extends IConditHandler> handlerCls, String suffix) {
        this.prefix = prefix;
        this.handlerCls = handlerCls;
        this.suffix = suffix;
    }

    /* ------------ getter 和 setter 方法 ------------- */

    public String getPrefix() {
        return prefix;
    }

    public Class<? extends IConditHandler> getHandlerCls() {
        return handlerCls;
    }

    public String getSuffix() {
        return suffix;
    }

}