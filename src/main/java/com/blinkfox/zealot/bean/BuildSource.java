package com.blinkfox.zealot.bean;

import org.dom4j.Node;

/**
 * 构建动态sql和参数相关的参数封装bean.
 * Created by blinkfox on 2016/10/30.
 */
public class BuildSource {

    // sql拼接信息
    private SqlInfo sqlInfo;

    // xml节点
    private Node node;

    // 参数对象
    private Object paramObj;

    // 前缀，默认空字符串
    private String prefix = "";

    /**
     * 全构造方法.
     * @param sqlInfo SQL拼接和参数对象
     * @param node 某查询zealot的dom4j的节点
     * @param paramObj 参数对象
     */
    public BuildSource(SqlInfo sqlInfo, Node node, Object paramObj) {
        super();
        this.sqlInfo = sqlInfo;
        this.node = node;
        this.paramObj = paramObj;
    }

    /* getter 和 setter 方法 */

    public SqlInfo getSqlInfo() {
        return sqlInfo;
    }

    public Node getNode() {
        return node;
    }

    public BuildSource setNode(Node node) {
        this.node = node;
        return this;
    }

    public Object getParamObj() {
        return paramObj;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}