package com.blinkfox.zealot.bean;

import java.util.List;
import com.blinkfox.zealot.helpers.StringHelper;

/**
 * 构造sql查询信息的拼接和参数对象
 * Created by blinkfox on 2016/10/30.
 */
public class SqlInfo {
	
	// 拼接sql的StringBuffer对象
    private StringBuilder join;

    // sql语句对应的有序参数
    private List<Object> params;

    /**
     * 构造方法
     * @param join
     */
    public SqlInfo(StringBuilder join) {
        this.join = join;
    }

    /**
     * 全构造方法
     * @param join
     * @param params
     */
    public SqlInfo(StringBuilder join, List<Object> params) {
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

    /* getter 和 setter 方法*/
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
	
}