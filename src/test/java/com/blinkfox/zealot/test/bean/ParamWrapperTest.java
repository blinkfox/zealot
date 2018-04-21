package com.blinkfox.zealot.test.bean;

import com.blinkfox.zealot.bean.ParamWrapper;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ParamWrapper的单元测试类.
 * @author blinkfox on 2017-08-16.
 */
public class ParamWrapperTest {

    private static Map<String, Object> paramMap;

    private static ParamWrapper paramWrapper;

    /**
     * 初始化ParamWrapper实例.
     */
    @BeforeClass
    public static void init() {
        paramMap = new HashMap<String, Object>();
        paramMap.put("name", "韩梅梅");
        paramMap.put("sex", "女");
        paramMap.put("age", 27);

        paramWrapper = ParamWrapper.newInstance();
    }

    /**
     * 测试构造新实例的方法.
     */
    @Test
    public void testNewInstance() {
        paramMap = ParamWrapper.newInstance(paramMap).put("birthday", "1990-05-18").toMap();
        Assert.assertEquals(4, paramMap.size());
        Assert.assertEquals(27, paramMap.get("age"));
    }

    /**
     * 测试put和get方法.
     */
    @Test
    public void testPut() {
        paramWrapper.put("aa", "张三").put("bb", "李四");
        Map<String, Object> paramMap = paramWrapper.toMap();
        Assert.assertEquals(2, paramMap.size());
        Assert.assertEquals("张三", paramMap.get("aa"));
    }

    /**
     * 销毁实例的方法.
     */
    @AfterClass
    public static void destroy() {
        paramWrapper = null;
    }

}