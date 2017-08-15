package com.blinkfox.zealot.core.builder;

import com.blinkfox.zealot.bean.BuildSource;

import java.util.Collection;

/**
 * 构建使用Java拼接sql片段的工具类.
 * Created by blinkfox on 2017-04-01.
 */
public class JavaSqlInfoBuilder extends SqlInfoBuilder {

    /**
     * 私有构造方法.
     */
    private JavaSqlInfoBuilder() {
        super();
    }

    /**
     * 获取JavaSqlInfoBuilder的实例，并初始化属性信息.
     * @param source BuildSource实例
     * @return JavaSqlInfoBuilder实例
     */
    public static JavaSqlInfoBuilder newInstace(BuildSource source) {
        JavaSqlInfoBuilder builder = new JavaSqlInfoBuilder();
        builder.init(source);
        return builder;
    }

    /**
     * 构建区间查询的sql信息.
     * @param fieldText 数据库字段文本
     * @param values 对象集合
     */
    public void buildInSqlByCollection(String fieldText, Collection<Object> values) {
        super.buildInSql(fieldText, values == null ? null : values.toArray());
    }

}