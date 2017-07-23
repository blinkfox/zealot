package com.blinkfox.zealot.core;

import com.blinkfox.zealot.bean.XmlContext;
import com.blinkfox.zealot.config.AbstractZealotConfig;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.exception.ConfigNotFoundException;
import com.blinkfox.zealot.exception.NodeNotFoundException;
import com.blinkfox.zealot.helpers.ParseHelper;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.helpers.XmlNodeHelper;
import com.blinkfox.zealot.loader.BannerLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zealot配置缓存管理器，用于加载Zealot Config配置信息到缓存中
 * Created by blinkfox on 2016/12/26.
 */
public class ZealotConfigManager {
    
    private static final Logger log = LoggerFactory.getLogger(ZealotConfigManager.class);

    /** 初始化的单实例. */
    private static final ZealotConfigManager confManager = new ZealotConfigManager();

    /**
     * 私有化构造方法.
     */
    private ZealotConfigManager() {
        super();
    }

    /**
     * 获取 ZealotConfigManager的唯一实例.
     * @return ZealotConfigManager唯一实例
     */
    public static ZealotConfigManager getInstance() {
        return confManager;
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     * @param configClass 系统中Zealot的class路径
     */
    public void initLoad(String configClass) {
        // 加载ZealotConfig配置信息
        this.loadZealotConfig(configClass);
        this.cachingXmlZealots();
        BannerLoader.newInstance().print();
        this.testFirstEvaluate();
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     * @param clazz 配置类
     */
    public void initLoad(Class<? extends AbstractZealotConfig> clazz) {
        this.initLoad(clazz.getName());
    }

    /**
     * 清空zealot所有缓存的内容.
     * 包括xml命名空间路径缓存、xml节点缓存
     */
    public void clear() {
        XmlContext.INSTANCE.getXmlDocMap().clear();
        AbstractZealotConfig.getZealots().clear();
    }

    /**
     * 初始化zealotConfig的子类，并加载配置信息.
     * @param configClass 配置类的class路径
     */
    private void loadZealotConfig(String configClass) {
        if (configClass == null) {
            throw new ConfigNotFoundException("未获取到ZealotConfig配置信息");
        }

        log.info("zealot加载器开始加载，Zealot配置类为:{}", configClass);
        Object temp;
        try {
            temp = Class.forName(configClass).newInstance();
        } catch (Exception e) {
            throw new ConfigNotFoundException("初始化zealotConfig实例失败,配置名称为:" + configClass, e);
        }

        // 判断获取到的类是否是AbstractZealotConfig的子类，如果是，则加载xml和自定义标签
        if (temp != null && temp instanceof AbstractZealotConfig) {
            this.loadZealotConfig((AbstractZealotConfig) temp);
        }
    }

    /**
     * 加载初始化zealotConfig的子类信息，并执行初始化mapper到缓存中.
     * @param zealotConfig 配置类
     */
    private void loadZealotConfig(AbstractZealotConfig zealotConfig) {
        zealotConfig.configXml(XmlContext.INSTANCE);
        zealotConfig.configTagHandler();
        log.warn("zealot的xml文件和tagHandler加载完成");
    }

    /**
     * 将每个zealotxml配置文件的key和文档缓存到ConcurrentHashMap内存缓存中.
     */
    @SuppressWarnings("unchecked")
    private void cachingXmlZealots() {
        Map<String, Document> xmlMaps = XmlContext.INSTANCE.getXmlDocMap();

        // 遍历所有的xml文档，将每个zealot节点缓存到ConcurrentHashMap内存缓存中
        for (Map.Entry<String, Document> entry: xmlMaps.entrySet()) {
            String nameSpace = entry.getKey();
            Document doc = entry.getValue();

            // 获取该文档下所有的zealot元素,
            List<Node> zealotNodes = doc.selectNodes(ZealotConst.ZEALOT_TAG);
            for (Node zealotNode: zealotNodes) {
                Node idNode = zealotNode.selectSingleNode(ZealotConst.ATTR_ID);
                String zealotId = XmlNodeHelper.getNodeText(idNode);
                if (StringHelper.isBlank(zealotId)) {
                    throw new NodeNotFoundException("未获取到zealot节点,zealotId为空!");
                }

                // zealot节点缓存到Map中，key是由nameSpace和zealot id组成,用"@"符号分隔,value是zealotNode
                String zealotKey = StringHelper.concat(nameSpace, ZealotConst.SP_AT, zealotId);
                AbstractZealotConfig.getZealots().put(zealotKey, zealotNode);
            }
        }
    }

    /**
     * 测试第一次MVEL表达式的计算,会缓存MVEL相关准备工作，加快后续的MVEL执行.
     */
    private void testFirstEvaluate() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("foo", "hello");
        ParseHelper.parseTemplate("@if{?foo != empty}Hello World!@end{}", context);
        ParseHelper.parseExpressWithException("foo != empty", context);
    }

}