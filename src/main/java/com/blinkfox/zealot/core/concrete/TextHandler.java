package com.blinkfox.zealot.core.concrete;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.IConditHandler;
import com.blinkfox.zealot.core.builder.XmlSqlInfoBuilder;
import com.blinkfox.zealot.exception.ContainXmlTagException;
import com.blinkfox.zealot.helpers.ParseHelper;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.helpers.XmlNodeHelper;

import java.util.List;

import org.dom4j.Node;

/**
 * 任意文本SQL及参数的处理器.
 * Created by blinkfox on 2017/4/15.
 */
public class TextHandler implements IConditHandler {

    /**
     * 构建等值查询的动态条件sql.
     * @param source 构建所需的资源对象
     * @return 返回SqlInfo对象
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        String valueText = XmlNodeHelper.getNodeAttrText(source.getNode(), ZealotConst.ATTR_VALUE);
        String matchText = XmlNodeHelper.getNodeAttrText(source.getNode(), ZealotConst.ATTR_MATCH);
        if (StringHelper.isBlank(matchText)) {
            return this.doBuildSqlInfo(source, valueText);
        } else {
            /* 如果match匹配成功，则生成数据库sql条件和参数 */
            Boolean isTrue = (Boolean) ParseHelper.parseExpressWithException(matchText, source.getParamObj());
            if (isTrue) {
                return this.doBuildSqlInfo(source, valueText);
            }
        }
        return source.getSqlInfo();
    }

    /**
     * 执行text标签SQL文本内容和有序参数的拼接.
     * @param source 构建所需的资源对象
     * @param valueText xml中value的文本内容
     * @return 返回SqlInfo对象
     */
    private SqlInfo doBuildSqlInfo(BuildSource source, String valueText) {
        SqlInfo sqlInfo = source.getSqlInfo();
        Node node = source.getNode();
        this.concatSqlText(node, sqlInfo);
        return XmlSqlInfoBuilder.newInstace(source).buildTextSqlParams(valueText);
    }

    /**
     * 构建<text></text>标签中sqlInfo中的SQL文本信息，如果有非文本节点则抛出异常.
     * 即text节点中的内容不能包含其他标签
     * @param node xml标签节点
     */
    @SuppressWarnings("unchecked")
    private void concatSqlText(Node node, SqlInfo sqlInfo) {
        // 获取所有子节点，并分别将其使用StringBuilder拼接起来
        List<Node> nodes = node.selectNodes(ZealotConst.ATTR_CHILD);
        for (Node n: nodes) {
            if (ZealotConst.NODETYPE_TEXT.equals(n.getNodeTypeName())) {
                // 如果子节点node 是文本节点，则直接获取其文本
                sqlInfo.getJoin().append(n.getText());
            } else {
                throw new ContainXmlTagException("<text></text>标签中不能包含其他xml标签，只能是文本元素！");
            }
        }
    }

}