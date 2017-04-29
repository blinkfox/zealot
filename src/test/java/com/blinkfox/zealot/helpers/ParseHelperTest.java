package com.blinkfox.zealot.helpers;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * ParseHelper的单元测试类.
 * Created by blinkfox on 2017/4/29.
 */
public class ParseHelperTest {

    private static Map<String, Object> context;

    /**
     * 初始化方法.
     */
    @BeforeClass
    public static void init() {
        context = new HashMap<String, Object>();
        context.put("hello", "world");
    }

    /**
     * 测试解析表达式的方法.
     */
    @Test
    public void testParseExpress() {
        assertEquals("world", ParseHelper.parseExpress("hello", context));
    }

    /**
     * 测试解析表达式会抛异常的方法.
     */
    @Test(expected = Exception.class)
    public void testParseExpressWithException() {
        assertEquals(null, ParseHelper.parseExpressWithException("abc", context));
    }

    /**
     * 测试解析表达式会抛异常的方法.
     */
    @Test(expected = Exception.class)
    public void testParseTemplate() {
        assertEquals("say world", ParseHelper.parseTemplate("say @{hello}", context));
        assertEquals(null, ParseHelper.parseTemplate("say @{abc}", context));
    }

}