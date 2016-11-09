package com.blinkfox.zealot.bean;

import java.util.ArrayList;
import java.util.List;
import com.blinkfox.zealot.helpers.StringHelper;

/**
 * 构造sql查询信息的拼接和参数对象
 * Created by blinkfox on 2016/10/30.
 */
public class SqlInfo {
	
	// 拼接sql的StringBuilder对象
    private StringBuilder join;

    // sql语句对应的有序参数
    private List<Object> params;

    /**
     * 全构造方法
     * @param join 拼接sql的StringBuilder对象
     * @param params 有序的参数集合
     */
    private SqlInfo(StringBuilder join, List<Object> params) {
        super();
        this.join = join;
        this.params = params;
    }

    /**
     * 获取一个新的SqlInfo实例
     * @return 返回SqlInfo实例
     */
    public static SqlInfo getNewInstance() {
        return new SqlInfo(new StringBuilder(""), new ArrayList<Object>());
    }

    /**
     * 获取最后拼接完成的sql
     * @return 返回拼接的sql
     */
    public String getSql() {
        return join == null ? "" : StringHelper.replaceBlank(join.toString());
    }

    /**
     * 得到参数的对象数组
     * @return 返回参数的对象数组
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