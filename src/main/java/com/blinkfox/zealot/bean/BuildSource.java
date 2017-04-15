package com.blinkfox.zealot.bean;

import com.blinkfox.zealot.consts.ZealotConst;
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

    // 前缀
    private String prefix;

    // 后缀，如：> = <=等
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

    /* getter 和 setter 方法 */

    public SqlInfo getSqlInfo() {
        return sqlInfo;
    }

    public BuildSource setSqlInfo(SqlInfo sqlInfo) {
        this.sqlInfo = sqlInfo;
        return this;
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

    public BuildSource setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public BuildSource setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

}