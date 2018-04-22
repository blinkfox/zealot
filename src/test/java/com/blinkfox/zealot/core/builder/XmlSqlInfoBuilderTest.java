package com.blinkfox.zealot.core.builder;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.ParamWrapper;
import com.blinkfox.zealot.bean.SqlInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * XmlSqlInfoBuilder的单元测试类.
 *
 * @author blinkfox on 2018-04-22.
 */
public class XmlSqlInfoBuilderTest {

    /**
     * 测试构建"in"SQL片段时value为null的情况的方法.
     */
    @Test
    public void buildInNullObjSql() {
        SqlInfo sqlInfo = XmlSqlInfoBuilder.newInstace(new BuildSource(SqlInfo.newInstance()))
                .buildInSql("a.name", "");
        Assert.assertEquals("", sqlInfo.getJoin().toString());
    }

    /**
     * 测试构建"in"SQL片段时传入为Collection集合时的方法.
     */
    @Test
    public void buildInListSql() {
        // 构建集合参数.
        List<Integer> sexs = new ArrayList<Integer>();
        sexs.add(0);
        sexs.add(1);
        Map<String, Object> paramMap = ParamWrapper.newInstance("sexs", sexs).toMap();

        SqlInfo sqlInfo = XmlSqlInfoBuilder.newInstace(new BuildSource(SqlInfo.newInstance()).setParamObj(paramMap))
                .buildInSql("a.n_sex", "sexs");
        Assert.assertEquals(" a.n_sex IN (?, ?) ", sqlInfo.getJoin().toString());
        assertArrayEquals(new Object[]{0, 1}, sqlInfo.getParamsArr());
    }

    /**
     * 测试构建"in"SQL片段时传入为Collection集合时的方法.
     */
    @Test
    public void buildInObjectSql() {
        // 构建集合参数.
        Map<String, Object> paramMap = ParamWrapper.newInstance("sexs", 1).toMap();

        SqlInfo sqlInfo = XmlSqlInfoBuilder.newInstace(new BuildSource(SqlInfo.newInstance()).setParamObj(paramMap))
                .buildInSql("a.n_sex", "sexs");
        Assert.assertEquals(" a.n_sex IN (?) ", sqlInfo.getJoin().toString());
        assertArrayEquals(new Object[]{1}, sqlInfo.getParamsArr());
    }

    /**
     * 测试构建"text"SQL片段的方法.
     */
    @Test
    public void buildTextSqlParams() {
        SqlInfo sqlInfo = XmlSqlInfoBuilder.newInstace(new BuildSource(SqlInfo.newInstance()))
                .buildTextSqlParams("");
        Assert.assertEquals("", sqlInfo.getJoin().toString());
    }
}