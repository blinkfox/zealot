package com.blinkfox.zealot.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 饿汉式单例的XML文件配置的上下文对象
 * Created by blinkfox on 2016-11-01.
 */
public class XmlContext {

    // 命名空间，对xml文件做唯一性标识
    private String nameSpace;

    // xml文件的路径
    private String filePath;

    // 所有zealots XML文档的缓存上下文的map,key是文件命名空间标识，value是资源路径
    private static Map<String, String> xmlMap = new ConcurrentHashMap<String, String>();

    /**
     * 私有构造方法
     */
    private XmlContext() {}

    private static final XmlContext xmlContext = new XmlContext();

    /**
     * 获取该对象实例
     * @return
     */
    public static XmlContext getInstance() {
        return xmlContext;
    }

    /**
     * xml上下文的构造方式
     * @param nameSpace
     * @param filePath
     */
    public void add(String nameSpace, String filePath) {
        xmlMap.put(nameSpace, filePath);
    }

    /**
     * 获取配置的xmlMap
     * @return
     */
    public static Map<String, String> getXmlMap() {
        return xmlMap;
    }

}