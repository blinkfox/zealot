package com.blinkfox.zealot.core;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.AbstractZealotConfig;
import com.blinkfox.zealot.config.entity.NormalConfig;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.exception.NodeNotFoundException;
import com.blinkfox.zealot.exception.ValidFailException;
import com.blinkfox.zealot.helpers.ParseHelper;
import com.blinkfox.zealot.helpers.SqlInfoPrinter;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.helpers.XmlNodeHelper;

import java.util.List;

import org.dom4j.Node;

/**
 * Zealot的核心解析和生成调用类.
 * Created by blinkfox on 2016/10/30.
 */
public final class Zealot {

    /**
     * 私有构造方法.
     */
    private Zealot() {
        super();
    }

    /**
     * 通过传入zealot xml文件对应的命名空间及zealot节点的ID的结合体，来简单快速的生成和获取sqlInfo信息(无参的SQL).
     *
     * @param nsAtZealotId xml命名空间nameSpace+@@+zealotId的值.如:"student@@queryStudentById"
     * @return 返回SqlInfo对象
     */
    public static SqlInfo getSqlInfoSimply(String nsAtZealotId) {
        return getSqlInfoSimply(nsAtZealotId, null);
    }

    /**
     * 通过传入zealot xml文件对应的命名空间、zealot节点的ID以及参数对象(一般是JavaBean或者Map)的结合体，来简单快速的生成和获取sqlInfo信息(有参的SQL).
     *
     * @param nsAtZealotId xml命名空间nameSpace+@@+zealotId的值.如:"student@@queryStudentById"
     * @param paramObj 参数对象(一般是JavaBean对象或者Map)
     * @return 返回SqlInfo对象
     */
    public static SqlInfo getSqlInfoSimply(String nsAtZealotId, Object paramObj) {
        String[] arr = nsAtZealotId.split(ZealotConst.SP_AT);
        if (arr.length != 2) {
            throw new  ValidFailException("nsAtZealotId参数的值必须是xml文件中的 nameSpace + '@@' + zealotId 节点的值，"
                    + "如:'student@@queryStudentById'.其中student为nameSpace, queryStudentById为XML文件中SQL的zealotId.");
        }
        return getSqlInfo(arr[0], arr[1], paramObj);
    }

    /**
     * 通过传入zealot xml文件对应的命名空间和zealot节点的ID来生成和获取sqlInfo信息(无参的SQL).
     *
     * @param nameSpace xml命名空间
     * @param zealotId xml中的zealotId
     * @return 返回SqlInfo对象
     */
    public static SqlInfo getSqlInfo(String nameSpace, String zealotId) {
        return getSqlInfo(nameSpace, zealotId, null);
    }

    /**
     * 通过传入zealot xml文件对应的命名空间、zealot节点的ID以及参数对象(一般是JavaBean或者Map)来生成和获取sqlInfo信息(有参的SQL).
     *
     * @param nameSpace xml命名空间
     * @param zealotId xml中的zealotId
     * @param paramObj 参数对象(一般是JavaBean对象或者Map)
     * @return 返回SqlInfo对象
     */
    public static SqlInfo getSqlInfo(String nameSpace, String zealotId, Object paramObj) {
        if (StringHelper.isBlank(nameSpace) || StringHelper.isBlank(zealotId)) {
            throw new ValidFailException("请输入有效的nameSpace或者zealotId的值!");
        }

        // 获取nameSpace文档中的指定sql的zealotId的节点对应的Node节点，如果是debug模式，则实时获取；否则从缓存中获取.
        Node zealotNode = NormalConfig.getInstance().isDebug() ? XmlNodeHelper.getNodeBySpaceAndId(nameSpace, zealotId)
                : AbstractZealotConfig.getZealots().get(StringHelper.concat(nameSpace, ZealotConst.SP_AT, zealotId));
        if (zealotNode == null) {
            throw new NodeNotFoundException("未找到nameSpace为:" + nameSpace + ",zealotId为:" + zealotId + "的节点!");
        }

        // 生成新的SqlInfo信息并打印出来.
        SqlInfo sqlInfo = buildNewSqlInfo(nameSpace, zealotNode, paramObj);
        SqlInfoPrinter.newInstance().printZealotSqlInfo(sqlInfo, true, nameSpace, zealotId);
        return sqlInfo;
    }

    /**
     * 构建完整的SqlInfo对象.
     *
     * @param nameSpace xml命名空间
     * @param sqlInfo SqlInfo对象
     * @param node dom4j对象节点
     * @param paramObj 参数对象
     * @return 返回SqlInfo对象
     */
    @SuppressWarnings("unchecked")
    public static SqlInfo buildSqlInfo(String nameSpace, SqlInfo sqlInfo, Node node, Object paramObj) {
        // 获取所有子节点，并分别将其使用StringBuilder拼接起来
        List<Node> nodes = node.selectNodes(ZealotConst.ATTR_CHILD);
        for (Node n: nodes) {
            if (ZealotConst.NODETYPE_TEXT.equals(n.getNodeTypeName())) {
                // 如果子节点node 是文本节点，则直接获取其文本
                sqlInfo.getJoin().append(n.getText());
            } else if (ZealotConst.NODETYPE_ELEMENT.equals(n.getNodeTypeName())) {
                // 如果子节点node 是元素节点，则再判断其是什么元素，动态判断条件和参数
                ConditContext.buildSqlInfo(new BuildSource(nameSpace, sqlInfo, n, paramObj), n.getName());
            }
        }

        return buildFinalSql(sqlInfo, paramObj);
    }

    /**
     * 构建新的、完整的SqlInfo对象.
     *
     * @param nameSpace xml命名空间
     * @param node dom4j对象节点
     * @param paramObj 参数对象
     * @return 返回SqlInfo对象
     */
    private static SqlInfo buildNewSqlInfo(String nameSpace, Node node, Object paramObj) {
        return buildSqlInfo(nameSpace, SqlInfo.newInstance(), node, paramObj);
    }

    /**
     * 根据标签拼接的SQL信息来生成最终的SQL.
     *
     * @param sqlInfo sql及参数信息
     * @param paramObj 参数对象信息
     * @return 返回SqlInfo对象
     */
    private static SqlInfo buildFinalSql(SqlInfo sqlInfo, Object paramObj) {
        // 得到生成的SQL，如果有MVEL的模板表达式，则执行计算出该表达式来生成最终的SQL
        String sql = sqlInfo.getJoin().toString();
        sql = ParseHelper.parseTemplate(sql, paramObj);
        return sqlInfo.setSql(StringHelper.replaceBlank(sql));
    }

}