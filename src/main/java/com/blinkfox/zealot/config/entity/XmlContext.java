package com.blinkfox.zealot.config.entity;

import com.blinkfox.zealot.helpers.StringHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 枚举单例的XML文件对应的Document配置的上下文.
 * Created by blinkfox on 2016-11-01.
 */
public enum XmlContext {

    /**
     * 唯一实例.
     */
    INSTANCE {

        /** 所有zealots XML文档的缓存上下文的map,key是文件命名空间标识，value是xml文件资源路径. */
        private final Map<String, String> xmlPathMap = new ConcurrentHashMap<String, String>();

        /**
         * 添加xml命名空间和文件路径到`ConcurrentHashMap`中.
         * @param nameSpace xml文件命名空间
         * @param filePath xml文件资源路径
         */
        @Override
        public void add(String nameSpace, String filePath) {
            if (StringHelper.isBlank(nameSpace) || StringHelper.isBlank(filePath)) {
                log.warn("Zealot的config中配置的命名空间标识或者XML文件路径为空!");
                return;
            }

            // 将XML命名空间的标识和其对应的document文档,用单例缓存起来.
            xmlPathMap.put(nameSpace, filePath);
        }

        /**
         * 获取key为xml命名空间,value为xml文件资源路径字符串的Map.
         * @return map
         */
        @Override
        public Map<String, String> getXmlPathMap() {
            return xmlPathMap;
        }

    };

    private static final Logger log = LoggerFactory.getLogger(XmlContext.class);

    /**
     * 添加xml命名空间和文件路径到 ConcurrentHashMap 中.
     * @param nameSpace xml文件命名空间
     * @param filePath xml文件资源路径
     */
    public abstract void add(String nameSpace, String filePath);

    /**
     * 获取key为xml命名空间,value为xml文件资源路径字符串的Map.
     * @return map
     */
    public abstract Map<String, String> getXmlPathMap();

}