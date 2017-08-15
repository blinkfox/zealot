package com.blinkfox.zealot.test.helpers;

import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**的
 * xml和xml节点相关工具类的单元测试类.
 * Created by blinkfox on 2017/7/23.
 */
public class XmlNodeHelperTest {

    /** zealot-user.xml文件的路径. */
    private static final String xmlPath = "zealot-user.xml";

    /** 全局XML格式的Document文档. */
    private static Document doc;

    /**
     * 初始化Document文档的方法.
     */
    @BeforeClass
    public static void init() {
        doc = XmlNodeHelper.getDocument(xmlPath);
    }

    /**
     * 测试获取的Document文档是否为空的方法.
     */
    @Test
    public void testGetDocument() {
        Assert.assertNotNull(doc);
    }

    /**
     * 测试根据xml文件docment中的zealot节点id值获取对应的第一个节点的方法.
     */
    @Test
    public void testGetZealotNodeById() {
        Node node = XmlNodeHelper.getZealotNodeById(doc, "queryUserInfos");
        Assert.assertNotNull(node);
    }

    /**
     * 测试获取Document文档中的Node节点文本值的方法.
     */
    @Test
    public void testGetNodeText() {
        Node node = XmlNodeHelper.getZealotNodeById(doc, "queryUserById");
        Assert.assertNotNull(node);

        String text = XmlNodeHelper.getNodeText(node);
        Assert.assertNotNull(text);
    }

    /**
     * 销毁实例的方法.
     */
    @AfterClass
    public static void destroy() {
        doc = null;
    }

}