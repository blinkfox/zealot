package com.blinkfox.zealot.core;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.builder.SqlInfoBuilder;

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
     * 执行生成等值查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotBuilder的当前实例
     */
    private ZealotBuilder doEqual(String prefix, String field, Object value, boolean match) {
        if (match) {
            SqlInfoBuilder.newInstace(source.setPrefix(prefix)).buildEqualSql(field, value);
            source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成like模糊查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotBuilder的当前实例
     */
    private ZealotBuilder doLike(String prefix, String field, Object value, boolean match) {
        if (match) {
            SqlInfoBuilder.newInstace(source.setPrefix(prefix)).buildLikeSql(field, value);
            source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成like模糊查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param startValue 值
     * @param endValue 值
     * @param match 是否匹配
     * @return ZealotBuilder的当前实例
     */
    private ZealotBuilder doBetween(String prefix, String field, Object startValue, Object endValue, boolean match) {
        if (match) {
            SqlInfoBuilder.newInstace(source.setPrefix(prefix)).buildBetweenSql(field, startValue, endValue);
            source.resetPrefix();
        }
        return this;
    }

    /**
     * 生成等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return SqlInfo
     */
    public ZealotBuilder equalled(String field, Object value) {
        return this.doEqual(ZealotConst.SPACE_PREFIX, field, value, true);
    }

    /**
     * 生成等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder equalled(String field, Object value, boolean match) {
        return this.doEqual(ZealotConst.SPACE_PREFIX, field, value, match);
    }

    /**
     * 生成带" AND "前缀等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return SqlInfo
     */
    public ZealotBuilder andEqual(String field, Object value) {
        return this.doEqual(ZealotConst.AND_PREFIX, field, value, true);
    }

    /**
     * 生成带" AND "前缀等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder andEqual(String field, Object value, boolean match) {
        return this.doEqual(ZealotConst.AND_PREFIX, field, value, match);
    }

    /**
     * 生成带" OR "前缀等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return SqlInfo
     */
    public ZealotBuilder orEqual(String field, Object value) {
        return this.doEqual(ZealotConst.OR_PREFIX, field, value, true);
    }

    /**
     * 生成带" OR "前缀等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder orEqual(String field, Object value, boolean match) {
        return this.doEqual(ZealotConst.OR_PREFIX, field, value, match);
    }

    /**
     * 生成like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return SqlInfo
     */
    public ZealotBuilder like(String field, Object value) {
        return this.doLike(ZealotConst.SPACE_PREFIX, field, value, true);
    }

    /**
     * 生成like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder like(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.SPACE_PREFIX, field, value, match);
    }

    /**
     * 生成带" AND "前缀的like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return SqlInfo
     */
    public ZealotBuilder andLike(String field, Object value) {
        return this.doLike(ZealotConst.AND_PREFIX, field, value, true);
    }

    /**
     * 生成带" AND "前缀的like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder andLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.AND_PREFIX, field, value, match);
    }

    /**
     * 生成带" OR "前缀的like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return SqlInfo
     */
    public ZealotBuilder orLike(String field, Object value) {
        return this.doLike(ZealotConst.OR_PREFIX, field, value, true);
    }

    /**
     * 生成带" OR "前缀的like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder orLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.OR_PREFIX, field, value, match);
    }

    /**
     * 生成between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return SqlInfo
     */
    public ZealotBuilder between(String field, Object startValue, Object endValue) {
        return this.doBetween(ZealotConst.SPACE_PREFIX, field, startValue, endValue, true);
    }

    /**
     * 生成between区间查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder between(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.SPACE_PREFIX, field, startValue, endValue, match);
    }

    /**
     * 生成带" AND "前缀的between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return SqlInfo
     */
    public ZealotBuilder andBetween(String field, Object startValue, Object endValue) {
        return this.doBetween(ZealotConst.AND_PREFIX, field, startValue, endValue, true);
    }

    /**
     * 生成带" AND "前缀的between区间查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder andBetween(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.AND_PREFIX, field, startValue, endValue, match);
    }

    /**
     * 生成带" OR "前缀的between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return SqlInfo
     */
    public ZealotBuilder orBetween(String field, Object startValue, Object endValue) {
        return this.doBetween(ZealotConst.OR_PREFIX, field, startValue, endValue, true);
    }

    /**
     * 生成带" OR "前缀的between区间查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return SqlInfo
     */
    public ZealotBuilder orBetween(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.OR_PREFIX, field, startValue, endValue, match);
    }

}