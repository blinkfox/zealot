package com.blinkfox.zealot.bean;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 * SqlInfo的单元测试类.
 * Created by blinkfox on 2017/4/24.
 */
public class SqlInfoTest {

    /** sqlInfo对象. */
    private static SqlInfo sqlInfo = null;

    /**
     * 初始化方法.
     */
    @BeforeClass
    public static void init() {
        sqlInfo = SqlInfo.newInstance();
    }

    /**
     * 测试生成实例的方法.
     */
    @Test
    public void testNewInstance() {
        assertEquals(0, sqlInfo.getJoin().length());
        assertArrayEquals(new Object[]{}, sqlInfo.getParamsArr());
    }

    /**
     * 测试获取参数并转换成数组的方法.
     */
    @Test
    public void testGetParamsArr() {
        sqlInfo.setParams(null);
        assertArrayEquals(new Object[]{}, sqlInfo.getParamsArr());
    }

}