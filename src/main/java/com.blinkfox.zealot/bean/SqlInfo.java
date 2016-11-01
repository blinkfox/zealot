package com.blinkfox.zealot.bean;

import com.blinkfox.zealot.helpers.StringHelper;

import java.util.ArrayList;

/**
 * 构造sql查询信息的拼接和参数对象
 * Created by blinkfox on 2016/10/30.
 */
public class SqlInfo {

    // 拼接sql的StringBuffer对象
    private StringBuffer join;

    // sql语句对应的有序参数
    private ArrayList<Object> params;

    /**
     * 构造方法
     * @param join
     */
    public SqlInfo(StringBuffer join) {
        this.join = join;
    }

    /**
     * 全构造方法
     * @param join
     * @param params
     */
    public SqlInfo(StringBuffer join, ArrayList<Object> params) {
        super();
        this.join = join;
        this.params = params;
    }

    /**
     * 获取最后拼接完成的sql
     * @return
     */
    public String getSql() {
        return join == null ? "" : StringHelper.replaceBlank(join.toString());
    }

    /**
     * 得到参数的对象数组
     * @return
     */
    public Object[] getParamsArr() {
        return params == null ? new Object[]{} : this.params.toArray();
    }

    /*getter和setter方法*/
    public StringBuffer getJoin() {
        return join;
    }
    public SqlInfo setJoin(StringBuffer join) {
        this.join = join;
        return this;
    }
    public ArrayList<Object> getParams() {
        return params;
    }
    public SqlInfo setParams(ArrayList<Object> params) {
        this.params = params;
        return this;
    }

}