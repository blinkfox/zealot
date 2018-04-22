package com.blinkfox.zealot.helpers;

import com.blinkfox.zealot.config.entity.XmlContext;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.exception.FieldEmptyException;
import com.blinkfox.zealot.exception.XmlParseException;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * xml和xml节点相关的工具类.
 * Created by blinkfox on 2016/10/30.
 */
public final class XmlNodeHelper {

    /**
     * 私有构造方法.
     */
    private XmlNodeHelper() {
        super();
    }

    /**
     * 读取xml文件为dom4j的Docment文档.
     * @param xmlPath 定位xml文件的路径，如：com/blinkfox/test.xml
     * @return 返回dom4j文档
     */
    public static Document getDocument(String xmlPath) {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlPath);
            return new SAXReader().read(is);
        } catch (Exception e) {
            throw new XmlParseException("读取或解析xml文件失败，xmlPath是:" + xmlPath, e);
        }
    }

    /**
     * 根据xml文件的nameSpace及zealot节点id值获取对应的第一个dom4j节点.
     * @param nameSpace xml文件对应命名空间
     * @param zealotId ZealotId
     * @return dom4j的Node节点
     */
    public static Node getNodeBySpaceAndId(String nameSpace, String zealotId) {
        String filePath = XmlContext.INSTANCE.getXmlPathMap().get(nameSpace);
        Document doc = XmlNodeHelper.getDocument(filePath);
        return doc == null ? null : XmlNodeHelper.getZealotNodeById(doc, zealotId);
    }

    /**
     * 根据xml文件docment中的zealot节点id值获取对应的第一个节点.
     * <p>使用xPath语法获取第一个符合条件的节点.</p>
     * @param doc docment文档
     * @param id zealotId
     * @return dom4j的Node节点
     */
    public static Node getZealotNodeById(Document doc, String id) {
        return doc.selectSingleNode("/zealots/zealot[@id='" + id + "']");
    }

    /**
     * 获取xml节点的文本值，如果对象是空的，则转为空字符串.
     * @param node dom4j节点
     * @return 返回节点文本值
     */
    public static String getNodeText(Node node) {
        return node == null ? "" : node.getText();
    }

    /**
     * 获取节点文本.
     * @param node dom4j节点
     * @param attrName 节点属性
     * @return 返回节点文本值
     */
    public static String getNodeAttrText(Node node, String attrName) {
        Node fieldNode = node.selectSingleNode(attrName);
        return XmlNodeHelper.getNodeText(fieldNode);
    }

    /**
     * 检查和获取节点文本，会检查节点是否为空，如果节点为空，则抛出异常.
     * @param node dom4j节点
     * @param nodeName 节点名称
     * @return 返回节点文本值
     */
    public static String getAndCheckNodeText(Node node, String nodeName) {
        /* 判断必填的参数是否为空 */
        Node fieldNode = node.selectSingleNode(nodeName);
        String fieldText = XmlNodeHelper.getNodeText(fieldNode);
        if (StringHelper.isBlank(fieldText)) {
            throw new FieldEmptyException("填写的字段值是空的");
        }
        return fieldText;
    }

    /**
     * 检查和获取开始和结束文本的内容，返回一个数组，会检查两个节点是否为空，如果都为空，则抛出异常.
     * @param node dom4j节点
     * @return 返回开始和结束文本的二元数组
     */
    public static String[] getBothCheckNodeText(Node node) {
        Node startNode = node.selectSingleNode(ZealotConst.ATTR_START);
        Node endNode = node.selectSingleNode(ZealotConst.ATTR_ENT);
        String startText = XmlNodeHelper.getNodeText(startNode);
        String endText = XmlNodeHelper.getNodeText(endNode);
        if (StringHelper.isBlank(startText) && StringHelper.isBlank(endText)) {
            throw new FieldEmptyException("填写的开始和结束字段值是空的");
        }
        return new String[] {startText, endText};
    }

}