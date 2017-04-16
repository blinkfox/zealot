package com.blinkfox.zealot.core;

import static com.blinkfox.zealot.consts.SqlKeyConst.*;

/**
 * 数据库查询拼接sql及关键字的工具类.
 * @deprecated 不推荐使用该类了，推荐使用ZealotKhala，功能更强大.
 */
@Deprecated
public class Khala {

    // sql拼接的StringBuilder对象
    private StringBuilder sql;

    /**
     * 私有构造方法.
     */
    private Khala() {
        this.sql = sql;
    }

    /**
     * 获取Khala的实例，命名风格类似单例，不推荐使用.
     * @deprecated 命名风格不对
     * @return Khala实例
     */
    @Deprecated
    public static Khala getInstance() {
        return new Khala();
    }

    /**
     * 获取Khala的实例.
     * @return Khala实例
     */
    public static Khala newInstance() {
        return new Khala();
    }

    /**
     * 开始将sql置为空字符串的StringBuffer.
     * @return khala对象本身
     */
    public Khala start() {
        this.sql = new StringBuilder("");
        return this;
    }

    /**
     * 获取sql的字符串,如果有多于两个空格，则将多个空格转成一个空格.
     * @return 拼接完成的sql字符串
     */
    public String end() {
        return sql == null ? "" : sql.toString().replaceAll("\\s{2,}", " ").trim();
    }

    /**
     * 连接字符串.
     * @param sql sql拼接器
     * @param sqlKey sql关键字
     * @param params 其他若干字符串参数
     */
    private void concat(StringBuilder sql, String sqlKey, String ... params) {
        sql.append(SPACE).append(sqlKey).append(SPACE);
        for (String s: params) {
            sql.append(s).append(SPACE);
        }
    }

    /**
     * 添加 select 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala select(String param) {
        concat(sql, SELECT, param);
        return this;
    }

    /**
     * 添加 from 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala from(String param) {
        concat(sql, FROM, param);
        return this;
    }

    /**
     * 添加 where 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala where(String param) {
        concat(sql, WHERE, param);
        return this;
    }

    /**
     * 添加 and 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala and(String param) {
        concat(sql, AND, param);
        return this;
    }

    /**
     * 添加 order by 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala orderBy(String ... param) {
        concat(sql, ORDER_BY, param);
        return this;
    }

    /**
     * 添加 left join 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala innerJoin(String param) {
        concat(sql, INNER_JOIN, param);
        return this;
    }

    /**
     * 添加 left join 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala leftJoin(String param) {
        concat(sql, LEFT_JOIN, param);
        return this;
    }

    /**
     * 添加 right join 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala rightJoin(String param) {
        concat(sql, RIGHT_JOIN, param);
        return this;
    }

    /**
     * 添加 full join 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala fullJoin(String param) {
        concat(sql, FULL_JOIN, param);
        return this;
    }

    /**
     * 添加 on 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     *
     */
    public Khala on(String param) {
        concat(sql, ON, param);
        return this;
    }

    /**
     * 添加 as 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala as(String param) {
        concat(sql, AS, param);
        return this;
    }

    /**
     * 添加 update 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala update(String param) {
        concat(sql, UPDATE, param);
        return this;
    }

    /**
     * 添加 set 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala set(String param) {
        concat(sql, SET, param);
        return this;
    }

    /**
     * 添加 delete 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala delete(String param) {
        concat(sql, DELETE, param);
        return this;
    }

    /**
     * 添加 insert into 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala insertInto(String param) {
        concat(sql, INSERT_INTO, param);
        return this;
    }

    /**
     * 添加 values 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala values(String param) {
        concat(sql, VALUES, param);
        return this;
    }

    /**
     * 添加 group by 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala groupBy(String param) {
        concat(sql, GROUP_BY, param);
        return this;
    }

    /**
     * 添加 having 字符串.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala having(String param) {
        concat(sql, HAVING, param);
        return this;
    }

    /**
     * 直接连接.
     * @param param 待拼接的sql片段字符串
     * @return khala对象本身
     */
    public Khala add(String param) {
        sql.append(param);
        return this;
    }

}