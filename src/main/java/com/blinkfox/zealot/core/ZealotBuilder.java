package com.blinkfox.zealot.core;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.core.builder.JavaSqlInfoBuilder;

/**
 * Zealot构造Java链式SQL和参数的类.
 * Created by blinkfox on 2017-03-31.
 */
public final class ZealotBuilder {

    // 封装了SqlInfo、应用中提供的上下文参数、前缀等信息.由于这里是纯Java拼接,所以就没有xml的Node节点信息，初始为为null即可
    private BuildSource source;

    /**
     * 私有构造方法.
     */
    private ZealotBuilder() {
        super();
    }

    /**
     * 开始的方法.
     * @return SqlInfo实例
     */
    public static ZealotBuilder start() {
        return new ZealotBuilder().setSource(new BuildSource(SqlInfo.newInstance(), null, null));
    }

    /**
     * 结束SQL拼接流程，并生成最终的sqlInfo信息.
     * @return sqlInfo
     */
    public SqlInfo end() {
        return this.getSource().getSqlInfo();
    }

    /**
     * 生成等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return SqlInfo
     */
    public ZealotBuilder equalled(String field, String value) {
        return setSource(source.setSqlInfo(JavaSqlInfoBuilder.newInstace(source).buildEqualSql(field, value)));
    }

    /**
     * 生成等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder equalled(String field, String value, boolean match) {
        if (match) {
            return equalled(field, value);
        }
        return this;
    }

    /**
     * source的getter方法.
     * @return BuildSource实例
     */
    public BuildSource getSource() {
        return source;
    }

    /**
     * source的setter方法.
     * @param source BuildSource实例
     * @return zealotBuilder
     */
    public ZealotBuilder setSource(BuildSource source) {
        this.source = source;
        return this;
    }

}