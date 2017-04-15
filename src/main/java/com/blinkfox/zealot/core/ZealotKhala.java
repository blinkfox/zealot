package com.blinkfox.zealot.core;

import static com.blinkfox.zealot.consts.SqlKeyConst.*;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.builder.JavaSqlInfoBuilder;
import com.blinkfox.zealot.core.builder.SqlInfoBuilder;
import com.blinkfox.zealot.core.inters.ICustomAction;
import com.blinkfox.zealot.exception.NotCollectionOrArrayException;
import com.blinkfox.zealot.helpers.CollectionHelper;
import com.blinkfox.zealot.helpers.StringHelper;
import java.util.Collection;
import java.util.Collections;

/**
 * 构造Zealot的Java链式SQL和参数的类.
 * Created by blinkfox on 2017-03-31.
 */
public final class ZealotKhala {

    // 封装了SqlInfo、应用中提供的上下文参数、前缀等信息.由于这里是纯Java拼接,所以就没有xml的Node节点信息，初始为为null
    private BuildSource source;

    /**
     * 私有构造方法，构造时就初始化BuildSource相应的参数信息.
     */
    private ZealotKhala() {
        this.source = new BuildSource(SqlInfo.newInstance());
    }

    /**
     * 开始的方法.
     * @return ZealotKhala实例
     */
    public static ZealotKhala start() {
        return new ZealotKhala();
    }

    /**
     * 结束SQL拼接流程，并生成最终的sqlInfo信息.
     * @return sqlInfo
     */
    public SqlInfo end() {
        SqlInfo sqlInfo = this.source.getSqlInfo();
        return sqlInfo.setSql(StringHelper.replaceBlank(sqlInfo.getJoin().toString()));
    }

    /**
     * 连接字符串.
     * @param sqlKey sql关键字
     * @param params 其他若干字符串参数
     */
    private ZealotKhala concat(String sqlKey, String ... params) {
        this.source.getSqlInfo().getJoin().append(SPACE).append(sqlKey).append(SPACE);
        if (params != null && params.length > 0) {
            for (String s: params) {
                this.source.getSqlInfo().getJoin().append(s).append(SPACE);
            }
        }
        return this;
    }

    /**
     * 拼接并带上'INSERT_INTO'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala insertInto(String text) {
        return concat(INSERT_INTO , text);
    }

    /**
     * 拼接并带上'VALUES'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala values(String text) {
        return concat(VALUES , text);
    }

    /**
     * 拼接并带上'DELETE FROM'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala deleteFrom(String text) {
        return concat(DELETE_FROM , text);
    }

    /**
     * 拼接并带上'UPDATE'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala update(String text) {
        return concat(UPDATE, text);
    }

    /**
     * 拼接并带上'SELECT'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala select(String text) {
        return concat(SELECT, text);
    }

    /**
     * 拼接并带上'FROM'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala from(String text) {
        return concat(FROM, text);
    }

    /**
     * 拼接并带上'WHERE'关键字的字符串和动态参数.
     * @param text 文本
     * @param value 参数值
     * @return ZealotKhala实例
     */
    public ZealotKhala where(String text, Object... value) {
        concat(WHERE, text);
        return this.param(value);
    }

    /**
     * 拼接并带上'AND'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala and(String text) {
        return concat(AND, text);
    }

    /**
     * 拼接并带上'OR'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala or(String text) {
        return concat(OR, text);
    }

    /**
     * 拼接并带上'AS'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala as(String text) {
        return concat(AS, text);
    }

    /**
     * 拼接并带上'AS'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala set(String text) {
        return concat(SET, text);
    }

    /**
     * 拼接并带上'INNER JOIN'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala innerJoin(String text) {
        return concat(INNER_JOIN, text);
    }

    /**
     * 拼接并带上'LEFT JOIN'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala leftJoin(String text) {
        return concat(LEFT_JOIN, text);
    }

    /**
     * 拼接并带上'RIGHT JOIN'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala rightJoin(String text) {
        return concat(RIGHT_JOIN, text);
    }

    /**
     * 拼接并带上'FULL JOIN'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala fullJoin(String text) {
        return concat(FULL_JOIN, text);
    }

    /**
     * 拼接并带上'ON'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala on(String text) {
        return concat(ON, text);
    }

    /**
     * 拼接并带上'ORDER BY'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala orderBy(String text) {
        return concat(ORDER_BY, text);
    }

    /**
     * 拼接并带上'GROUP BY'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala groupBy(String text) {
        return concat(GROUP_BY, text);
    }

    /**
     * 拼接并带上'HAVING'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala having(String text) {
        return concat(HAVING, text);
    }

    /**
     * 拼接并带上'LIMIT'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala limit(String text) {
        return concat(LIMIT, text);
    }

    /**
     * 拼接并带上'OFFSET'关键字的字符串.
     * @param text 文本
     * @return ZealotKhala实例
     */
    public ZealotKhala offset(String text) {
        return concat(OFFSET, text);
    }

    /**
     * 拼接并带上'ASC'关键字的字符串.
     * @return ZealotKhala实例
     */
    public ZealotKhala asc() {
        return concat(ASC);
    }

    /**
     * 拼接并带上'DESC'关键字的字符串.
     * @return ZealotKhala实例
     */
    public ZealotKhala desc() {
        return concat(DESC);
    }

    /**
     * 拼接并带上'UNION'关键字的字符串.
     * @return ZealotKhala实例
     */
    public ZealotKhala union() {
        return concat(UNION);
    }

    /**
     * 拼接并带上'UNION ALL'关键字的字符串.
     * @return ZealotKhala实例
     */
    public ZealotKhala unionAll() {
        return concat(UNION_ALL);
    }

    /**
     * 在sql后追加任何文本字符串，后可追加自定义可变参数.
     * @param text 文本
     * @param values 可变参数数组
     * @return ZealotKhala实例
     */
    public ZealotKhala text(String text, Object... values) {
        this.source.getSqlInfo().getJoin().append(text);
        this.appendParams(values, ZealotConst.OBJTYPE_ARRAY);
        return this;
    }

    /**
     * 在sql的参数集合后追加任何的数组.
     * @param value 值
     * @param objType 对象类型那
     * @return ZealotKhala实例
     */
    private ZealotKhala appendParams(Object value, int objType) {
        Object[] values = CollectionHelper.toArray(value, objType);
        if (CollectionHelper.isNotEmpty(values)) {
            Collections.addAll(this.source.getSqlInfo().getParams(), values);
        }
        return this;
    }

    /**
     * 在sql的参数集合后追加不定对象个数的数组.
     * @param values 不定个数的值，也是数组
     * @return ZealotKhala实例
     */
    public ZealotKhala param(Object... values) {
        return this.appendParams(values, ZealotConst.OBJTYPE_ARRAY);
    }

    /**
     * 在sql的参数集合后追加任何的一个集合.
     * @param values 不定个数的值
     * @return ZealotKhala实例
     */
    public ZealotKhala param(Collection<?> values) {
        return this.appendParams(values, ZealotConst.OBJTYPE_COLLECTION);
    }

    /**
     * 当匹配match条件为true时，即执行文本拼接和参数传递.
     * @param match 匹配条件
     * @param text 文本
     * @param values 不定参数的值
     * @return ZealotKhala实例
     */
    public ZealotKhala when(boolean match, String text, Object... values) {
        return match ? text(text, values) : this;
    }

    /**
     * 执行自定义的任意操作.
     * @param action 执行when条件中的方法
     * @return ZealotKhala实例
     */
    public ZealotKhala doAnything(ICustomAction action) {
        SqlInfo sqlInfo = this.source.getSqlInfo();
        action.execute(sqlInfo.getJoin(), sqlInfo.getParams());
        return this;
    }

    /**
     * 当匹配match条件为true时，才执行自定义的任意操作.
     * @param match 匹配条件
     * @param action 执行when条件中的方法
     * @return ZealotKhala实例
     */
    public ZealotKhala doAnything(boolean match, ICustomAction action) {
        return match ? this.doAnything(action) : this;
    }

    /**
     * 执行生成等值查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 值
     * @param suffix 后缀
     * @param match 是否匹配
     * @return ZealotKhala实例的当前实例
     */
    private ZealotKhala doNormal(String prefix, String field, Object value, String suffix, boolean match) {
        if (match) {
            SqlInfoBuilder.newInstace(this.source.setPrefix(prefix)).buildNormalSql(field, value, suffix);
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成like模糊查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例的当前实例
     */
    private ZealotKhala doLike(String prefix, String field, Object value, boolean match) {
        if (match) {
            SqlInfoBuilder.newInstace(this.source.setPrefix(prefix)).buildLikeSql(field, value);
            this.source.resetPrefix();
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
     * @return ZealotKhala实例的当前实例
     */
    private ZealotKhala doBetween(String prefix, String field, Object startValue, Object endValue, boolean match) {
        if (match) {
            SqlInfoBuilder.newInstace(this.source.setPrefix(prefix)).buildBetweenSql(field, startValue, endValue);
            this.source.resetPrefix();
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
     * @return ZealotKhala实例的当前实例
     */
    @SuppressWarnings("unchecked")
    private ZealotKhala doInByType(String prefix, String field, Object value, boolean match, int objType) {
        if (match) {
            // 根据对象类型调用对应的生成in查询的sql片段方法,否则抛出类型不符合的异常
            switch (objType) {
                case ZealotConst.OBJTYPE_ARRAY:
                    SqlInfoBuilder.newInstace(this.source.setPrefix(prefix)).buildInSql(field, (Object[]) value);
                    break;
                case ZealotConst.OBJTYPE_COLLECTION:
                    JavaSqlInfoBuilder.newInstace(this.source.setPrefix(prefix))
                            .buildInSqlByCollection(field, (Collection<Object>) value);
                    break;
                default:
                    throw new NotCollectionOrArrayException("in查询的值不是有效的集合或数组!");
            }
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成in范围查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return ZealotKhala实例的当前实例
     */
    private ZealotKhala doIn(String prefix, String field, Object[] values, boolean match) {
        return this.doInByType(prefix, field, values, match, ZealotConst.OBJTYPE_ARRAY);
    }

    /**
     * 执行生成in范围查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return ZealotKhala实例的当前实例
     */
    private ZealotKhala doIn(String prefix, String field, Collection<?> values, boolean match) {
        return this.doInByType(prefix, field, values, match, ZealotConst.OBJTYPE_COLLECTION);
    }

    /**
     * 生成等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala equal(String field, Object value) {
        return this.doNormal(ZealotConst.SPACE_PREFIX, field, value, ZealotConst.EQUAL_SUFFIX,true);
    }

    /**
     * 生成等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala equal(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.SPACE_PREFIX, field, value, ZealotConst.EQUAL_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala andEqual(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.EQUAL_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala andEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.EQUAL_SUFFIX, match);
    }

    /**
     * 生成带" OR "前缀等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala orEqual(String field, Object value) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.EQUAL_SUFFIX, true);
    }

    /**
     * 生成带" OR "前缀等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala orEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.EQUAL_SUFFIX, match);
    }

    /**
     * 生成大于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala greaterThan(String field, Object value) {
        return this.doNormal(ZealotConst.SPACE_PREFIX, field, value, ZealotConst.GT_SUFFIX, true);
    }

    /**
     * 生成大于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala greaterThan(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.SPACE_PREFIX, field, value, ZealotConst.GT_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀大于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala andGreaterThan(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.GT_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀大于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala andGreaterThan(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.GT_SUFFIX, match);
    }

    /**
     * 生成小于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala lessThan(String field, Object value) {
        return this.doNormal(ZealotConst.SPACE_PREFIX, field, value, ZealotConst.LT_SUFFIX, true);
    }

    /**
     * 生成小于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala lessThan(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.SPACE_PREFIX, field, value, ZealotConst.LT_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀小于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala andLessThan(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.LT_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀小于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala andLessThan(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.LT_SUFFIX, match);
    }

    /**
     * 生成大于等于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala greaterEqual(String field, Object value) {
        return this.doNormal(ZealotConst.SPACE_PREFIX, field, value, ZealotConst.GTE_SUFFIX, true);
    }

    /**
     * 生成大于等于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala greaterEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.SPACE_PREFIX, field, value, ZealotConst.GTE_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀大于等于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala andGreaterEqual(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.GTE_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀大于等于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala andGreaterEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.GTE_SUFFIX, match);
    }

    /**
     * 生成小于等于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala lessEqual(String field, Object value) {
        return this.doNormal(ZealotConst.SPACE_PREFIX, field, value, ZealotConst.LTE_SUFFIX, true);
    }

    /**
     * 生成小于等于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala lessEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.SPACE_PREFIX, field, value, ZealotConst.LTE_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀小于等于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala andLessEqual(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.LTE_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀小于等于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala andLessEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.LTE_SUFFIX, match);
    }

    /**
     * 生成like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala like(String field, Object value) {
        return this.doLike(ZealotConst.SPACE_PREFIX, field, value, true);
    }

    /**
     * 生成like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala like(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.SPACE_PREFIX, field, value, match);
    }

    /**
     * 生成带" AND "前缀的like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala andLike(String field, Object value) {
        return this.doLike(ZealotConst.AND_PREFIX, field, value, true);
    }

    /**
     * 生成带" AND "前缀的like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala andLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.AND_PREFIX, field, value, match);
    }

    /**
     * 生成带" OR "前缀的like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return ZealotKhala实例
     */
    public ZealotKhala orLike(String field, Object value) {
        return this.doLike(ZealotConst.OR_PREFIX, field, value, true);
    }

    /**
     * 生成带" OR "前缀的like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala orLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.OR_PREFIX, field, value, match);
    }

    /**
     * 生成between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return ZealotKhala实例
     */
    public ZealotKhala between(String field, Object startValue, Object endValue) {
        return this.doBetween(ZealotConst.SPACE_PREFIX, field, startValue, endValue, true);
    }

    /**
     * 生成between区间查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala between(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.SPACE_PREFIX, field, startValue, endValue, match);
    }

    /**
     * 生成带" AND "前缀的between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return ZealotKhala实例
     */
    public ZealotKhala andBetween(String field, Object startValue, Object endValue) {
        return this.doBetween(ZealotConst.AND_PREFIX, field, startValue, endValue, true);
    }

    /**
     * 生成带" AND "前缀的between区间查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala andBetween(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.AND_PREFIX, field, startValue, endValue, match);
    }

    /**
     * 生成带" OR "前缀的between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return ZealotKhala实例
     */
    public ZealotKhala orBetween(String field, Object startValue, Object endValue) {
        return this.doBetween(ZealotConst.OR_PREFIX, field, startValue, endValue, true);
    }

    /**
     * 生成带" OR "前缀的between区间查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala orBetween(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.OR_PREFIX, field, startValue, endValue, match);
    }

    /**
     * 生成in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return ZealotKhala实例
     */
    public ZealotKhala in(String field, Object[] values) {
        return this.doIn(ZealotConst.SPACE_PREFIX, field, values, true);
    }

    /**
     * 生成in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala in(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.SPACE_PREFIX, field, values, match);
    }

    /**
     * 生成in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return ZealotKhala实例
     */
    public ZealotKhala in(String field, Collection<?> values) {
        return this.doIn(ZealotConst.SPACE_PREFIX, field, values, true);
    }

    /**
     * 生成in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala in(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.SPACE_PREFIX, field, values, match);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return ZealotKhala实例
     */
    public ZealotKhala andIn(String field, Object[] values) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, true);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala andIn(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, match);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return ZealotKhala实例
     */
    public ZealotKhala andIn(String field, Collection<?> values) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, true);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala andIn(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, match);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return ZealotKhala实例
     */
    public ZealotKhala orIn(String field, Object[] values) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, true);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala orIn(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, match);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return ZealotKhala实例
     */
    public ZealotKhala orIn(String field, Collection<?> values) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, true);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return ZealotKhala实例
     */
    public ZealotKhala orIn(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, match);
    }

}