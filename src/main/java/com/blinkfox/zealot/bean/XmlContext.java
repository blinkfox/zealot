package com.blinkfox.zealot.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 饿汉式单例的XML文件配置的上下文对象
 * Created by blinkfox on 2016-11-01.
 */
public class XmlContext {
	
    // 所有zealots XML文档的缓存上下文的map,key是文件命名空间标识，value是资源路径
    private static Map<String, String> xmlMap = new ConcurrentHashMap<String, String>();

    // 生成全局的xmlContext单例对象
    private static final XmlContext xmlContext = new XmlContext();

    /**
     * 私有构造方法
     */
    private XmlContext() {
    	
    }

    /**
     * 获取该对象实例
     * @return 返回xmlContext实例
     */
    public static XmlContext getInstance() {
        return xmlContext;
    }

    /**
     * xml上下文的构造方式
     * @param nameSpace xml文件命名空间
     * @param filePath xml文件资源路径
     */
    public void add(String nameSpace, String filePath) {
        xmlMap.put(nameSpace, filePath);
    }

    /**
     * 获取配置的xmlMap
     * @return 返回xml文档的命名空间、路径缓存上下文的map
     */
    public static Map<String, String> getXmlMap() {
        return xmlMap;
    }
	
}