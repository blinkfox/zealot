package com.blinkfox.zealot.helpers;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * dom4j相关的工具类
 * Created by blinkfox on 2016/10/30.
 */
public class Dom4jHelper {

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
                System.out.println("------已成功解析document元素");
            }
        } catch (DocumentException e) {
            System.out.println("-----读取xml文件失败，xmlPath是：" + xmlPath);
            e.printStackTrace();
        }
        return document;
    }

}