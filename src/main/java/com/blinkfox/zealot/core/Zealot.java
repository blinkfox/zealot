package com.blinkfox.zealot.core;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Node;
import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.AbstractZealotConfig;
import com.blinkfox.zealot.consts.ZealotConst;

/**
 * Zealot的核心解析和生成调用类
 * Created by blinkfox on 2016/10/30.
 */
public class Zealot {
	
	/**
	 * 私有构造方法
	 */
	private Zealot() {
		super();
	}

	/**
     * 获取sqlInfo信息
     * @param nameSpace
     * @param zealotId
     * @param paramObj
     * @return
     */
    public static SqlInfo getSqlInfo(String nameSpace, String zealotId, Object paramObj) {
        Document doc = AbstractZealotConfig.getZealots().get(nameSpace);
        if (doc == null) {
            throw new RuntimeException("未获取到xml文档,nameSpace 为：" + nameSpace);
        }

        // 获取文档的指定sql的zealotId的节点
        String xPath = "//zealot[@id='" + zealotId +"']";
        Node node = doc.selectSingleNode(xPath);
        if (node == null) {
            throw new RuntimeException("未获取到zealot节点,zealotId为：" + zealotId);
        }

        return buildSqlInfo(node, paramObj);
    }

    /**
     * 构建完整的SqlInfo对象
     * @param node
     * @param paramObj
     * @return
     */
    @SuppressWarnings("unchecked")
	private static SqlInfo buildSqlInfo(Node node, Object paramObj) {
        SqlInfo sqlInfo = new SqlInfo(new StringBuilder(""), new ArrayList<Object>());
        StringBuilder join = sqlInfo.getJoin();

        List<Node> nodes = node.selectNodes(ZealotConst.ATTR_CHILD);
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            if (ZealotConst.NODETYPE_TEXT.equals(n.getNodeTypeName())) {
                // 如果子节点node 是文本节点，则直接获取其文本
                join.append(n.getText());
            } else if (ZealotConst.NODETYPE_ELEMENT.equals(n.getNodeTypeName())) {
                BuildSource source = new BuildSource(sqlInfo, n, paramObj);
                // 如果子节点node 是元素节点，则再判断其是什么元素，动态判断条件和参数
                sqlInfo = ConditContext.buildSqlInfo(source, n.getName());
            }
        }

        return sqlInfo;
    }
	
}