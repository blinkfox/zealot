package com.blinkfox.zealot.config.entity;

import org.junit.Test;

/**
 * XmlContext的单元测试类.
 *
 * @author blinkfox on 2018-4-22.
 */
public class XmlContextTest {

    /**
     * 测试add方法nameSpace和filePath均为空时的情形.
     */
    @Test
    public void add() {
        XmlContext.INSTANCE.add("", null);
    }
}