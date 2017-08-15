package com.blinkfox.zealot.test.bean;

import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * BuildSource测试类.
 * Created by blinkfox on 2017/4/24.
 */
public class BuildSourceTest {

    /** 构建SQL的资源. */
    private static BuildSource source;

    /**
     * 初始化方法.
     */
    @BeforeClass
    public static void init() {
        source = new BuildSource(SqlInfo.newInstance());
    }

    /**
     * 重置前缀的方法.
     */
    @Test
    public void testResetPrefix() {
        source.setPrefix(null).resetPrefix();
        Assert.assertEquals(ZealotConst.ONE_SPACE, source.getPrefix());
    }

    /**
     * 重置后缀的方法.
     */
    @Test
    public void testResetSuffix() {
        source.setSuffix(null).resetSuffix();
        assertEquals(ZealotConst.ONE_SPACE, source.getSuffix());
    }

    /**
     * 测试设置SqlInfo对象的方法.
     */
    @Test
    public void testSetSqlInfo() {
        SqlInfo sqlInfo = SqlInfo.newInstance();
        sqlInfo.setJoin(new StringBuilder("SELECT id, name").append(" FROM user"));
        source.setSqlInfo(sqlInfo);
        assertEquals("SELECT id, name FROM user", source.getSqlInfo().getJoin().toString());
    }

    /**
     * 测试设置Node节点的方法.
     */
    @Test
    public void testSetNode() {
        source.setNode(null);
        assertEquals(null, source.getNode());
    }

}