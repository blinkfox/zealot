package com.blinkfox.zealot.bean;

import com.blinkfox.zealot.consts.ZealotConst;

import org.dom4j.Node;

/**
 * 构建动态sql和参数相关的封装实体类.
 * Created by blinkfox on 2016/10/30.
 */
public final class BuildSource {

    /** xml文件对应的命名空间. */
    private String nameSpace;

    /** SQL拼接信息. */
    private SqlInfo sqlInfo;

    /** XML节点. */
    private Node node;

    /** 参数对象上下文，一般为Bean或者Map. */
    private Object paramObj;

    /** 拼接SQL片段的前缀，如:and、or等. */
    private String prefix;

    /** 拼接SQL片段的后缀，如：> = <=等. */
    private String suffix;

    /**
     * 仅仅有sqlInfo的构造方法.
     * @param sqlInfo SQL拼接和参数对象
     */
    public BuildSource(SqlInfo sqlInfo) {
        super();
        this.sqlInfo = sqlInfo;
        resetPrefix();
        resetSuffix();
    }

    /**
     * 含SqlInfo、Node节点、参数上下文的构造方法.
     * @param nameSpace 命名空间
     * @param sqlInfo SQL拼接和参数对象
     * @param node 某查询zealot的dom4j的节点
     * @param paramObj 参数对象
     */
    public BuildSource(String nameSpace, SqlInfo sqlInfo, Node node, Object paramObj) {
        super();
        this.sqlInfo = sqlInfo;
        this.node = node;
        this.paramObj = paramObj;
        resetPrefix();
        resetSuffix();
    }

    /**
     * 重置前缀为默认值.
     * 为了防止SQL拼接时连在一起，默认前缀为一个空格的字符串.
     */
    public void resetPrefix() {
        this.prefix = ZealotConst.ONE_SPACE;
    }

    /**
     * 重置后缀为默认值.
     * 为了防止SQL拼接时连在一起，默认后缀为一个空格的字符串.
     */
    public void resetSuffix() {
        this.suffix = ZealotConst.ONE_SPACE;
    }

    /* --------------- 以下是 getter 和 setter 方法. ---------------- */

    /**
     * 获取sqlInfo的getter方法.
     * @return SqlInfo实例
     */
    public SqlInfo getSqlInfo() {
        return sqlInfo;
    }

    /**
     * 设置sqlInfo的setter方法.
     * @param sqlInfo SqlInfo实例
     * @return 当前BuildSource的实例
     */
    public BuildSource setSqlInfo(SqlInfo sqlInfo) {
        this.sqlInfo = sqlInfo;
        return this;
    }

    /**
     * 获取node的getter方法.
     * @return Node实例
     */
    public Node getNode() {
        return node;
    }

    /**
     * 设置node的setter方法.
     * @param node Node实例
     * @return 当前BuildSource的实例
     */
    public BuildSource setNode(Node node) {
        this.node = node;
        return this;
    }

    /**
     * 获取paramObj的getter方法.
     * @return paramObj对象
     */
    public Object getParamObj() {
        return paramObj;
    }

    /**
     * 获取前缀prefix的getter方法.
     * @return prefix对象
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * 设置prefix的setter方法.
     * @param prefix prefix对象
     * @return 当前BuildSource的实例
     */
    public BuildSource setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * 获取后缀suffix的getter方法.
     * @return suffix对象
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * 设置后缀suffix的setter方法.
     * @param suffix suffix对象
     * @return 当前BuildSource的实例
     */
    public BuildSource setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

}