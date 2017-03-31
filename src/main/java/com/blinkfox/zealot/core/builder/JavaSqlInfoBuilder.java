package com.blinkfox.zealot.core.builder;

import com.blinkfox.zealot.bean.BuildSource;

/**
 * 构建使用Java拼接sql片段的工具类.
 * Created by blinkfox on 2017-03-31.
 */
public final class JavaSqlInfoBuilder extends SqlInfoBuilder {

    /**
     * 私有构造方法.
     */
    private JavaSqlInfoBuilder() {
        super();
    }

    /**
     * 获取JavaSqlInfoBuilder的实例，并初始化属性信息.
     * @param source BuildSource实例
     * @return XmlSqlInfoBuilder实例
     */
    public static JavaSqlInfoBuilder newInstace(BuildSource source) {
        JavaSqlInfoBuilder builder = new JavaSqlInfoBuilder();
        builder.init(source);
        return builder;
    }

    /**
     * 构建普通等值查询的sql和参数.
     * @param fieldText 字段文本值
     * @param value 参数值
     */
    public void buildEqualSql(String fieldText, Object value) {
        super.doBuildEqualSql(fieldText, value);
    }

    /**
     * 构建like模糊查询的sql和参数.
     * @param fieldText 字段文本值
     * @param value 参数值
     */
    public void buildLikeSql(String fieldText, Object value) {
        super.doBuildLikeSql(fieldText, value);
    }

    /**
     * 构建like模糊查询的sql和参数.
     * @param fieldText 字段文本值
     * @param startValue 开始值
     * @param endValue 结束值
     */
    public void buildBetweenSql(String fieldText, Object startValue, Object endValue) {
        super.doBuildBetweenSql(fieldText, startValue, endValue);
    }

}