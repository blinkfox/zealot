package com.blinkfox.zealot.test.core.builder;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.core.builder.SqlInfoBuilder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * SqlInfoBuilder单元测试类.
 * Created by blinkfox on 2017/4/29.
 */
public class SqlInfoBuilderTest {

    /** BuildSource实例. */
    private BuildSource source;

    /**
     * 每个方法执行之前都初始化BuildSource实例.
     */
    @Before
    public void initSource() {
        source = new BuildSource(SqlInfo.newInstance()).setPrefix("");
    }

    /**
     * 每个方法执行之后都销毁BuildSource实例.
     */
    @After
    public void destroySource() {
        source = null;
    }

    /**
     * 构建按like自定义模式来生成sql片段的测试方法.
     */
    @Test
    public void testBuildLikePatternSql() {
        SqlInfo sqlInfo = SqlInfoBuilder.newInstace(source)
                .buildLikePatternSql("b.title", "Java%");

        Assert.assertEquals("b.title LIKE 'Java%' ", sqlInfo.getJoin().toString());
        Assert.assertArrayEquals(new Object[]{}, sqlInfo.getParamsArr());
    }

    /**
     * 构建区间查询的测试方法.
     */
    @Test
    public void testBuildBetweenSql() {
        SqlInfo sqlInfo = SqlInfoBuilder.newInstace(source)
                .buildBetweenSql("u.age", null, 20);

        Assert.assertEquals("u.age <= ? ", sqlInfo.getJoin().toString());
        Assert.assertArrayEquals(new Object[]{20}, sqlInfo.getParamsArr());
    }

    /**
     * 构建区间查询的测试方法2.
     */
    @Test
    public void testBuildBetweenSql2() {
        SqlInfo sqlInfo = SqlInfoBuilder.newInstace(source)
                .buildBetweenSql("u.age", 15, null);

        Assert.assertEquals("u.age >= ? ", sqlInfo.getJoin().toString());
        Assert.assertArrayEquals(new Object[]{15}, sqlInfo.getParamsArr());
    }

    /**
     * 构建区间查询的测试方法2.
     */
    @Test
    public void testBuildInSql() {
        SqlInfo sqlInfo = SqlInfoBuilder.newInstace(source)
                .buildInSql("u.sex", null);

        Assert.assertEquals("", sqlInfo.getJoin().toString());
        Assert.assertArrayEquals(new Object[]{}, sqlInfo.getParamsArr());
    }

}