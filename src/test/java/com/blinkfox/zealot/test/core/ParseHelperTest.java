package com.blinkfox.zealot.test.core;

import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.test.helpers.ParseHelper;
import com.blinkfox.zealot.test.log.Log;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 表达式和模版解析的单元测试类.
 * Created by blinkfox on 2016/12/26.
 */
public class ParseHelperTest {

    private static final Log log = Log.get(ParseHelperTest.class);

    /**
     * 测试计算表达式的值.
     */
    @Test
    public void testParseWithMvel() {
        // 构造查询的参数
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("foo", "Hello");
        context.put("bar", "World");
        String result = (String) ParseHelper.parseExpressWithException("foo + bar", context);
        log.info("testParseWithMvel 结果:" + result);
        assertEquals("HelloWorld", result);
    }

    /**
     * 测试计算模版的值.
     */
    @Test
    public void testParseTemplate() {
        // 构造查询的参数
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("foo", "Hello");
        String result = ParseHelper.parseTemplate("@if{?foo != empty}@{foo} World!@end{}", context);
        log.info("testParseTemplate 结果:{}" + result);
        assertEquals("Hello World!", result);
    }

}