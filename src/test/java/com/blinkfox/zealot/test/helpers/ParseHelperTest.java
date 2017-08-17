package com.blinkfox.zealot.test.helpers;

import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.bean.ParamWrapper;
import com.blinkfox.zealot.helpers.ParseHelper;
import com.blinkfox.zealot.log.Log;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ParseHelper的单元测试类.
 * Created by blinkfox on 2017/4/29.
 */
public class ParseHelperTest {

    private static final Log log = Log.get(ParseHelperTest.class);

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

    /**
     * 测试解析表达式会抛异常的方法.
     */
    @Test
    public void testIsMatch() {
        assertEquals(true, ParseHelper.isMatch(null, context));
        assertEquals(true, ParseHelper.isMatch("", context));
        assertEquals(true, ParseHelper.isMatch("hello != null", context));
        assertEquals(false, ParseHelper.isMatch("?hello != 'world'", context));
    }

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
     * 测试解析普通字符串的方法.
     */
    @Test
    public void testParseStr() {
        String result = ParseHelper.parseTemplate("zhangsan", ParamWrapper.newInstance("aa", "张三").toMap());
        log.info("testParseStr方法的结果:" + result);
        assertEquals("zhangsan", result);
    }

    /**
     * 测试解析普通字符串的方法.
     */
    @Test
    public void testParseStr2() {
        Boolean result = (Boolean) ParseHelper.parseExpress("sex == 1", ParamWrapper.newInstance("sex", "1").toMap());
        assertEquals(true, result);
    }

    /**
     * 测试解析普通字符串的方法.
     */
    @Test
    public void testParseSpaceStr() {
        Boolean result = (Boolean) ParseHelper.parseExpress("", ParamWrapper.newInstance("bb", "1").toMap());
        assertEquals(null, result);
    }

    /**
     * 测试计算模版的值.
     */
    @Test
    public void testParseTemplate2() {
        // 构造查询的参数
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("foo", "Hello");
        String result = ParseHelper.parseTemplate("@if{?foo != empty}@{foo} World!@end{}", context);
        log.info("testParseTemplate 结果:{}" + result);
        assertEquals("Hello World!", result);
    }

}