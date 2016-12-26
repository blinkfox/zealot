package com.blinkfox.zealot.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举单例的XML文件配置的上下文对象
 * Created by blinkfox on 2016-11-01.
 */
public enum XmlContext {

    /**
     * 唯一实例
     */
    INSTANCE {

        // 所有zealots XML文档的缓存上下文的map,key是文件命名空间标识，value是资源路径
        private Map<String, String> xmlMap = new ConcurrentHashMap<String, String>();

        @Override
        public void add(String nameSpace, String filePath) {
            xmlMap.put(nameSpace, filePath);
        }

        @Override
        public Map<String, String> getXmlMap() {
            return xmlMap;
        }

    };

    /**
     * 添加xml命名空间和文件路径到 ConcurrentHashMap 中
     * @param nameSpace xml文件命名空间
     * @param filePath xml文件资源路径
     */
    public abstract void add(String nameSpace, String filePath);

    /**
     * 获取存放xml命名空间和路径配置信息的xmlMap
     * @return 返回xml文档的命名空间、路径缓存上下文的map
     */
    public abstract Map<String, String> getXmlMap();
	
}