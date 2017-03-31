package com.blinkfox.zealot.bean;

import com.blinkfox.zealot.consts.ZealotConst;

/**
 * 标签和对应的动态sql生成的处理类.
 * Created by blinkfox on 2016-11-01.
 */
public class TagHandler {

    // 标签名称
    private String tagName;

    // 生成sql的前缀,如:and, or 等
    private String prefix;

    // 生成动态sql的处理实现反射类型，如:EqualHandler
    private Class<?> handlerCls;

    /**
     * 仅仅标签名称和其对应的处理类的构造方法.
     * @param tagName 标签名称
     * @param handlerCls 动态处理类的反射类型
     */
    public TagHandler(String tagName, Class<?> handlerCls) {
        this.tagName = tagName;
        this.prefix = ZealotConst.SPACE;
        this.handlerCls = handlerCls;
    }

    /**
     * 全构造方法.
     * @param tagName 标签名称
     * @param prefix sql前缀
     * @param handlerCls 动态处理类的反射类型
     */
    public TagHandler(String tagName, String prefix, Class<?> handlerCls) {
        this.tagName = tagName;
        this.prefix = prefix;
        this.handlerCls = handlerCls;
    }

    /* getter 和 setter 方法 */
    public String getTagName() {
        return tagName;
    }

    public String getPrefix() {
        return prefix;
    }

    public Class<?> getHandlerCls() {
        return handlerCls;
    }

}