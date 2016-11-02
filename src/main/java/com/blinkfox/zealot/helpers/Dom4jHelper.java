package com.blinkfox.zealot.helpers;

import java.io.InputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import com.blinkfox.zealot.consts.ZealotConst;

/**
 * dom4j相关的工具类
 * Created by blinkfox on 2016/10/30.
 */
public class Dom4jHelper {
	
	/**
	 * 私有构造方法
	 */
	private Dom4jHelper() {
		super();
	}

	/**
     * 读取xml文档
     * @param xmlPath 定位xml文件的路径，如：com/blinkfox/test.xml
     * @return
     */
    public static Document getDocument(String xmlPath) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlPath);
            if (is != null) {
                document = reader.read(is);
            }
        } catch (DocumentException e) {
            System.err.println("-----读取xml文件失败，xmlPath是：" + xmlPath);
            e.printStackTrace();
        }
        return document;
    }
    
    /**
     * 获取xml节点的文本值，如果是对象是空的，则转为空字符串
     * @param node
     * @return
     */
    public static String getNodeText(Node node) {
        return node == null ? "": node.getText();
    }

    /**
     * 检查节点是否为空
     * @param node
     * @return
     */
    public static String getAndCheckNodeText(Node node, String nodeName) {
		/* 判断必填的参数是否为空 */
        Node fieldNode = node.selectSingleNode(nodeName);
        String fieldText = Dom4jHelper.getNodeText(fieldNode);
        if (StringHelper.isBlank(fieldText)) {
            throw new RuntimeException("填写的字段值是空的");
        }
        return fieldText;
    }

    /**
     * 检查和获取开始和结束文本的内容，返回一个数组
     * @param node
     * @return
     */
    public static String[] getBothNodeText(Node node) {
        Node startNode = node.selectSingleNode(ZealotConst.ATTR_START);
        Node endNode = node.selectSingleNode(ZealotConst.ATTR_ENT);
        String startText = Dom4jHelper.getNodeText(startNode);
        String endText = Dom4jHelper.getNodeText(endNode);
        if (StringHelper.isBlank(startText) && StringHelper.isBlank(endText)) {
            throw new RuntimeException("填写的开始和结束字段值是空的");
        }
        return new String[] {startText, endText};
    }
	
}