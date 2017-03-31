package com.blinkfox.zealot.core;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.builder.JavaSqlInfoBuilder;

/**
 * Zealot构造Java链式SQL和参数的类.
 * Created by blinkfox on 2017-03-31.
 */
public final class ZealotBuilder {

    // 封装了SqlInfo、应用中提供的上下文参数、前缀等信息.由于这里是纯Java拼接,所以就没有xml的Node节点信息，初始为为null即可
    private static BuildSource source;

    /**
     * 私有构造方法，构造时就初始化BuildSource相应的参数信息.
     */
    private ZealotBuilder() {
        source = new BuildSource(SqlInfo.newInstance());
    }

    /**
     * 开始的方法.
     * @return SqlInfo实例
     */
    public static ZealotBuilder start() {
        return new ZealotBuilder();
    }

    /**
     * 结束SQL拼接流程，并生成最终的sqlInfo信息.
     * @return sqlInfo
     */
    public SqlInfo end() {
        return source.getSqlInfo();
    }

    /**
     * 生成等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return SqlInfo
     */
    public ZealotBuilder equalled(String field, Object value) {
        return doEqual(ZealotConst.SPACE_PREFIX, field, value, true);
    }

    /**
     * 生成等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder equalled(String field, Object value, boolean match) {
        return doEqual(ZealotConst.SPACE_PREFIX, field, value, match);
    }

    /**
     * 生成等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return SqlInfo
     */
    public ZealotBuilder andEqual(String field, Object value) {
        return doEqual(ZealotConst.AND_PREFIX, field, value, true);
    }

    /**
     * 生成等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder andEqual(String field, Object value, boolean match) {
        return doEqual(ZealotConst.AND_PREFIX, field, value, match);
    }

    /**
     * 生成等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return SqlInfo
     */
    public ZealotBuilder orEqual(String field, Object value) {
        return doEqual(ZealotConst.OR_PREFIX, field, value, true);
    }

    /**
     * 生成等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder orEqual(String field, Object value, boolean match) {
        return doEqual(ZealotConst.OR_PREFIX, field, value, match);
    }

    /**
     * 执行生成等值查询SQL的片段.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotBuilder的当前实例
     */
    private ZealotBuilder doEqual(String prefix, String field, Object value, boolean match) {
        if (match) {
            JavaSqlInfoBuilder.newInstace(source.setPrefix(prefix)).buildEqualSql(field, value);
            source.resetPrefix();
        }
        return this;
    }

}