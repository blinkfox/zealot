package com.blinkfox.zealot.test.bean;

import com.blinkfox.zealot.bean.ParamWrapper;

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

    private static ParamWrapper paramWrapper;

    /**
     * 初始化ParamWrapper实例.
     */
    @BeforeClass
    public static void init() {
        paramWrapper = ParamWrapper.newInstance();
    }

    /**
     * 测试put和get方法.
     */
    @Test
    public void testPut() {
        paramWrapper.put("aa", "张三").put("bb", "李四");
        Map<String, Object> paramMap = paramWrapper.getParamMap();
        Assert.assertEquals(paramMap.size(), 2);
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