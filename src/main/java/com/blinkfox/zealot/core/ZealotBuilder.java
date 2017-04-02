package com.blinkfox.zealot.core;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.builder.JavaSqlInfoBuilder;
import com.blinkfox.zealot.core.builder.SqlInfoBuilder;
import com.blinkfox.zealot.exception.NotCollectionOrArrayException;
import com.blinkfox.zealot.helpers.CollectionHelper;
import com.blinkfox.zealot.helpers.StringHelper;
import java.util.Collection;
import java.util.Collections;

import static com.blinkfox.zealot.consts.SqlKeyConst.*;


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
     * @return ZealotBuilder实例
     */
    public static ZealotBuilder start() {
        return new ZealotBuilder();
    }

    /**
     * 结束SQL拼接流程，并生成最终的sqlInfo信息.
     * @return sqlInfo
     */
    public SqlInfo end() {
        SqlInfo sqlInfo = source.getSqlInfo();
        return sqlInfo.setSql(StringHelper.replaceBlank(sqlInfo.getJoin().toString()));
    }

    /**
     * 连接字符串.
     * @param sqlKey sql关键字
     * @param params 其他若干字符串参数
     */
    private ZealotBuilder concat(String sqlKey, String ... params) {
        source.getSqlInfo().getJoin().append(SPACE).append(sqlKey).append(SPACE);
        if (params != null && params.length > 0) {
            for (String s: params) {
                source.getSqlInfo().getJoin().append(s).append(SPACE);
            }
        }
        return this;
    }

    /**
     * 拼接并带上'INSERT_INTO'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder insertInto(String text) {
        return concat(INSERT_INTO , text);
    }

    /**
     * 拼接并带上'VALUES'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder values(String text) {
        return concat(VALUES , text);
    }

    /**
     * 拼接并带上'DELETE FROM'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder deleteFrom(String text) {
        return concat(DELETE_FROM , text);
    }

    /**
     * 拼接并带上'UPDATE'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder update(String text) {
        return concat(UPDATE, text);
    }

    /**
     * 拼接并带上'SELECT'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder select(String text) {
        return concat(SELECT, text);
    }

    /**
     * 拼接并带上'FROM'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder from(String text) {
        return concat(FROM, text);
    }

    /**
     * 拼接并带上'WHERE'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder where(String text) {
        return concat(WHERE, text);
    }

    /**
     * 拼接并带上'AND'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder and(String text) {
        return concat(AND, text);
    }

    /**
     * 拼接并带上'AS'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder as(String text) {
        return concat(AS, text);
    }

    /**
     * 拼接并带上'AS'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder set(String text) {
        return concat(SET, text);
    }

    /**
     * 拼接并带上'INNER JOIN'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder innerJoin(String text) {
        return concat(INNER_JOIN, text);
    }

    /**
     * 拼接并带上'LEFT JOIN'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder leftJoin(String text) {
        return concat(LEFT_JOIN, text);
    }

    /**
     * 拼接并带上'RIGHT JOIN'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder rightJoin(String text) {
        return concat(RIGHT_JOIN, text);
    }

    /**
     * 拼接并带上'FULL JOIN'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder fullJoin(String text) {
        return concat(FULL_JOIN, text);
    }

    /**
     * 拼接并带上'ON'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder on(String text) {
        return concat(ON, text);
    }

    /**
     * 拼接并带上'ORDER BY'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder orderBy(String text) {
        return concat(ORDER_BY, text);
    }

    /**
     * 拼接并带上'GROUP BY'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder groupBy(String text) {
        return concat(GROUP_BY, text);
    }

    /**
     * 拼接并带上'HAVING'关键字的字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder having(String text) {
        return concat(HAVING, text);
    }

    /**
     * 拼接并带上'ASC'关键字的字符串.
     * @return ZealotBuilder
     */
    public ZealotBuilder asc() {
        return concat(ASC);
    }

    /**
     * 拼接并带上'DESC'关键字的字符串.
     * @return ZealotBuilder
     */
    public ZealotBuilder desc() {
        return concat(DESC);
    }

    /**
     * 在sql后追加任何文本字符串.
     * @param text 文本
     * @return ZealotBuilder
     */
    public ZealotBuilder text(String text) {
        source.getSqlInfo().getJoin().append(text);
        return this;
    }

    /**
     * 在sql的参数集合后追加任何的数组.
     * @param value 值
     * @param objType 对象类型那
     * @return ZealotBuilder
     */
    private ZealotBuilder appendParams(Object value, int objType) {
        Object[] values = CollectionHelper.toArray(value, objType);
        if (CollectionHelper.isNotEmpty(values)) {
            Collections.addAll(source.getSqlInfo().getParams(), values);
        }
        return this;
    }

    /**
     * 在sql的参数集合后追加任何的单个参数值.
     * @param value 单个值
     * @return ZealotBuilder
     */
    public ZealotBuilder param(Object value) {
        return this.appendParams(value, ZealotConst.OBJTYPE_OBJECT);
    }

    /**
     * 在sql的参数集合后追加不定对象个数的数组.
     * @param value 不定个数的值，也是数组
     * @return ZealotBuilder
     */
    public ZealotBuilder param(Object... value) {
        return this.appendParams(value, ZealotConst.OBJTYPE_ARRAY);
    }

    /**
     * 在sql的参数集合后追加任何的一个集合.
     * @param values 不定个数的值
     * @return ZealotBuilder
     */
    public ZealotBuilder param(Collection<?> values) {
        return this.appendParams(values, ZealotConst.OBJTYPE_COLLECTION);
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
     * 执行生成in范围查询SQL片段的方法,如果是集合或数组，则执行生成，否则抛出异常.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 数组的值
     * @param match 是否匹配
     * @param objType 对象类型，取自ZealotConst.java中以OBJTYPE开头的类型
     * @return ZealotBuilder的当前实例
     */
    @SuppressWarnings("unchecked")
    private ZealotBuilder doInByType(String prefix, String field, Object value, boolean match, int objType) {
        if (match) {
            // 根据对象类型调用对应的生成in查询的sql片段方法,否则抛出类型不符合的异常
            switch (objType) {
                case ZealotConst.OBJTYPE_ARRAY:
                    SqlInfoBuilder.newInstace(source.setPrefix(prefix)).buildInSql(field, (Object[]) value);
                    break;
                case ZealotConst.OBJTYPE_COLLECTION:
                    JavaSqlInfoBuilder.newInstace(source.setPrefix(prefix))
                            .buildInSqlByCollection(field, (Collection<Object>) value);
                    break;
                default:
                    throw new NotCollectionOrArrayException("in查询的值不是有效的集合或数组!");
            }
            source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成in范围查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return ZealotBuilder的当前实例
     */
    private ZealotBuilder doIn(String prefix, String field, Object[] values, boolean match) {
        return this.doInByType(prefix, field, values, match, ZealotConst.OBJTYPE_ARRAY);
    }

    /**
     * 执行生成in范围查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return ZealotBuilder的当前实例
     */
    private ZealotBuilder doIn(String prefix, String field, Collection<?> values, boolean match) {
        return this.doInByType(prefix, field, values, match, ZealotConst.OBJTYPE_COLLECTION);
    }

    /**
     * 生成等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotBuilder
     */
    public ZealotBuilder equalled(String field, Object value) {
        return this.doEqual(ZealotConst.SPACE_PREFIX, field, value, true);
    }

    /**
     * 生成等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder equalled(String field, Object value, boolean match) {
        return this.doEqual(ZealotConst.SPACE_PREFIX, field, value, match);
    }

    /**
     * 生成带" AND "前缀等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotBuilder
     */
    public ZealotBuilder andEqual(String field, Object value) {
        return this.doEqual(ZealotConst.AND_PREFIX, field, value, true);
    }

    /**
     * 生成带" AND "前缀等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder andEqual(String field, Object value, boolean match) {
        return this.doEqual(ZealotConst.AND_PREFIX, field, value, match);
    }

    /**
     * 生成带" OR "前缀等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotBuilder
     */
    public ZealotBuilder orEqual(String field, Object value) {
        return this.doEqual(ZealotConst.OR_PREFIX, field, value, true);
    }

    /**
     * 生成带" OR "前缀等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder orEqual(String field, Object value, boolean match) {
        return this.doEqual(ZealotConst.OR_PREFIX, field, value, match);
    }

    /**
     * 生成like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotBuilder
     */
    public ZealotBuilder like(String field, Object value) {
        return this.doLike(ZealotConst.SPACE_PREFIX, field, value, true);
    }

    /**
     * 生成like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder like(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.SPACE_PREFIX, field, value, match);
    }

    /**
     * 生成带" AND "前缀的like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotBuilder
     */
    public ZealotBuilder andLike(String field, Object value) {
        return this.doLike(ZealotConst.AND_PREFIX, field, value, true);
    }

    /**
     * 生成带" AND "前缀的like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder andLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.AND_PREFIX, field, value, match);
    }

    /**
     * 生成带" OR "前缀的like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotBuilder
     */
    public ZealotBuilder orLike(String field, Object value) {
        return this.doLike(ZealotConst.OR_PREFIX, field, value, true);
    }

    /**
     * 生成带" OR "前缀的like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder orLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.OR_PREFIX, field, value, match);
    }

    /**
     * 生成between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return ZealotBuilder
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
     * @return ZealotBuilder
     */
    public ZealotBuilder between(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.SPACE_PREFIX, field, startValue, endValue, match);
    }

    /**
     * 生成带" AND "前缀的between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return ZealotBuilder
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
     * @return ZealotBuilder
     */
    public ZealotBuilder andBetween(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.AND_PREFIX, field, startValue, endValue, match);
    }

    /**
     * 生成带" OR "前缀的between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return ZealotBuilder
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
     * @return ZealotBuilder
     */
    public ZealotBuilder orBetween(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.OR_PREFIX, field, startValue, endValue, match);
    }

    /**
     * 生成in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return ZealotBuilder
     */
    public ZealotBuilder in(String field, Object[] values) {
        return this.doIn(ZealotConst.SPACE_PREFIX, field, values, true);
    }

    /**
     * 生成in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder in(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.SPACE_PREFIX, field, values, match);
    }

    /**
     * 生成in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return ZealotBuilder
     */
    public ZealotBuilder in(String field, Collection<?> values) {
        return this.doIn(ZealotConst.SPACE_PREFIX, field, values, true);
    }

    /**
     * 生成in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder in(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.SPACE_PREFIX, field, values, match);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return ZealotBuilder
     */
    public ZealotBuilder andIn(String field, Object[] values) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, true);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder andIn(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, match);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return ZealotBuilder
     */
    public ZealotBuilder andIn(String field, Collection<?> values) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, true);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder andIn(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, match);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return ZealotBuilder
     */
    public ZealotBuilder orIn(String field, Object[] values) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, true);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder orIn(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, match);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return ZealotBuilder
     */
    public ZealotBuilder orIn(String field, Collection<?> values) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, true);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return ZealotBuilder
     */
    public ZealotBuilder orIn(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, match);
    }

}