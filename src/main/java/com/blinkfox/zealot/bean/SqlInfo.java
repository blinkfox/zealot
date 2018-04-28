package com.blinkfox.zealot.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造sql查询信息的拼接和参数对象.
 * Created by blinkfox on 2016/10/30.
 */
public class SqlInfo {

    /** 拼接sql的StringBuilder对象. */
    private StringBuilder join;

    /** sql语句对应的有序参数. */
    private List<Object> params;

    /** 最终生成的可用sql. */
    private String sql;

    /**
     * 全构造方法.
     * @param join 拼接sql的StringBuilder对象
     * @param params 有序的参数集合
     */
    private SqlInfo(StringBuilder join, List<Object> params) {
        super();
        this.join = join;
        this.params = params;
    }

    /**
     * 获取一个新的SqlInfo实例.
     * @return 返回SqlInfo实例
     */
    public static SqlInfo newInstance() {
        return new SqlInfo(new StringBuilder(""), new ArrayList<Object>());
    }

    /**
     * 得到参数的对象数组.
     * @return 返回参数的对象数组
     */
    public Object[] getParamsArr() {
        return params == null ? new Object[]{} : this.params.toArray();
    }

    /**
     * 如果存在某子SQL字符串，则移除该子SQL字符串，常用于来消除'WHERE 1=1'或其他不需要的SQL字符串的场景.
     * 注意该方法不会移除其对应的参数，所以，这里只应该移除静态SQL字符串，不应该移除包含占位符的SQL.
     *
     * @param subSql 静态子SQL片段
     * @return SqlInfo实例
     */
    public SqlInfo removeIfExist(String subSql) {
        this.sql = subSql != null && sql.contains(subSql) ? sql.replaceAll(subSql, "") : sql;
        return this;
    }

    /* -------------- 以下是 getter 和 setter 方法 ------------- */

    public StringBuilder getJoin() {
        return join;
    }

    public SqlInfo setJoin(StringBuilder join) {
        this.join = join;
        return this;
    }

    public List<Object> getParams() {
        return params;
    }

    public SqlInfo setParams(List<Object> params) {
        this.params = params;
        return this;
    }

    public String getSql() {
        return sql;
    }

    public SqlInfo setSql(String sql) {
        this.sql = sql;
        return this;
    }

}