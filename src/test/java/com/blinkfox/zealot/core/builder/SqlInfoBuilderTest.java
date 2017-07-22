package com.blinkfox.zealot.core.builder;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;

import org.junit.Assert;
import org.junit.Test;

/**
 * SqlInfoBuilder单元测试类.
 * Created by blinkfox on 2017/4/29.
 */
public class SqlInfoBuilderTest {

    /**
     * 构建区间查询的测试方法.
     */
    @Test
    public void testBuildBetweenSql() {
        BuildSource source = new BuildSource(SqlInfo.newInstance()).setPrefix("");
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
        BuildSource source = new BuildSource(SqlInfo.newInstance()).setPrefix("");
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
        BuildSource source = new BuildSource(SqlInfo.newInstance()).setPrefix("");
        SqlInfo sqlInfo = SqlInfoBuilder.newInstace(source)
                .buildInSql("u.sex", null);

        Assert.assertEquals("", sqlInfo.getJoin().toString());
        Assert.assertArrayEquals(new Object[]{}, sqlInfo.getParamsArr());
    }

}