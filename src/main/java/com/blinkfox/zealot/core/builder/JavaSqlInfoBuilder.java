package com.blinkfox.zealot.core.builder;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;

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
     * 构建普通等值查询的sql信息.
     * @param fieldText 字段文本值
     * @param valueText 参数值
     * @return 返回SqlInfo信息
     */
    public SqlInfo buildEqualSql(String fieldText, String valueText) {
        params.add(valueText);
        return buildEqualJoin(fieldText).setParams(params);
    }

}