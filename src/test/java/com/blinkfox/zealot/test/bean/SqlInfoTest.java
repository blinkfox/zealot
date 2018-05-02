package com.blinkfox.zealot.test.bean;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.bean.SqlInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * SqlInfo的单元测试类.
 * @author blinkfox on 2017/4/24.
 */
public class SqlInfoTest {

    /** sqlInfo对象. */
    private static SqlInfo sqlInfo = null;

    @Before
    public void init() {
        sqlInfo = SqlInfo.newInstance();
    }

    @After
    public void destroy() {
        sqlInfo = null;
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

    /**
     * 测试移除某些字符串.
     */
    @Test
    public void testRemoveIfExist() {
        // 初始化参数.
        String sql = "SELECT * FROM t_user WHERE 1 = 1 AND name LIKE 'zhang%' 1 <> 1 OR age = 13";
        sqlInfo.setSql(sql);

        sqlInfo.removeIfExist(" 1 = 1 AND").removeIfExist(" 1 <> 1").removeIfExist(" abc  ");
        assertEquals("SELECT * FROM t_user WHERE name LIKE 'zhang%' OR age = 13", sqlInfo.getSql());
    }

}