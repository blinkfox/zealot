package com.blinkfox.zealot.config;

import com.blinkfox.zealot.config.entity.NormalConfig;
import com.blinkfox.zealot.config.entity.XmlContext;
import com.blinkfox.zealot.config.scanner.TaggerScanner;
import com.blinkfox.zealot.config.scanner.XmlScanner;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.exception.ConfigNotFoundException;
import com.blinkfox.zealot.exception.NodeNotFoundException;
import com.blinkfox.zealot.helpers.ParseHelper;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.helpers.XmlNodeHelper;
import com.blinkfox.zealot.loader.BannerLoader;
import com.blinkfox.zealot.log.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;

/**
 * Zealot配置缓存管理器，用于加载Zealot Config配置信息到缓存中
 * @author blinkfox on 2016/12/26.
 */
public class ZealotConfigManager {
    
    private static final Log log = Log.get(ZealotConfigManager.class);

    /** 初始化的单实例. */
    private static final ZealotConfigManager confManager = new ZealotConfigManager();

    /** zealot的XML文件所在的位置，多个用逗号隔开,可以是目录也可以是具体的xml文件. */
    private String xmlLocations;

    /** zealot的自定义handler处理器所在的位置，多个用逗号隔开,可以是目录也可以是具体的java或class文件路径. */
    private String handlerLocations;

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
     *
     * @param configClass 系统中Zealot的class路径
     * @param xmlLocations zealot的XML文件所在的位置，多个用逗号隔开
     * @param handlerLocations zealot的自定义handler处理器所在的位置，多个用逗号隔开
     */
    public void initLoad(String configClass, String xmlLocations, String handlerLocations) {
        // 设置配置的文件路径的值，并开始加载ZealotConfig配置信息
        this.xmlLocations = xmlLocations;
        this.handlerLocations = handlerLocations;
        this.initLoad(configClass);
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     * @param clazz 配置类
     * @param xmlLocations zealot的XML文件所在的位置，多个用逗号隔开
     * @param handlerLocations zealot的自定义handler处理器所在的位置，多个用逗号隔开
     */
    public void initLoad(Class<? extends AbstractZealotConfig> clazz, String xmlLocations,
            String handlerLocations) {
        this.initLoad(clazz.getName(), xmlLocations, handlerLocations);
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     *
     * @param zealotConfig 配置类实例
     * @param xmlLocations zealot的XML文件所在的位置，多个用逗号隔开
     * @param handlerLocations zealot的自定义handler处理器所在的位置，多个用逗号隔开
     */
    public void initLoad(AbstractZealotConfig zealotConfig, String xmlLocations, String handlerLocations) {
        this.xmlLocations = xmlLocations;
        this.handlerLocations = handlerLocations;
        this.initLoad(zealotConfig);
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     * @param clazz 配置类
     */
    public void initLoad(Class<? extends AbstractZealotConfig> clazz) {
        this.initLoad(clazz.getName());
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     * @param configClass 系统中Zealot的class路径
     */
    public void initLoad(String configClass) {
        // 加载ZealotConfig配置信息
        this.scanLocations(this.xmlLocations, this.handlerLocations);
        this.loadZealotConfig(configClass);
        cachingXmlAndEval();
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     *
     * @param zealotConfig 配置类实例
     */
    public void initLoad(AbstractZealotConfig zealotConfig) {
        this.scanLocations(this.xmlLocations, this.handlerLocations);
        this.loadZealotConfig(zealotConfig);
        this.cachingXmlAndEval();
    }

    /**
     * 扫描 xml和handler所在的文件位置.
     * @param xmlLocations zealot的XML文件所在的位置
     * @param handlerLocations zealot的自定义handler处理器所在的位置
     */
    private void scanLocations(String xmlLocations, String handlerLocations) {
        this.xmlLocations = StringHelper.isBlank(this.xmlLocations) ? "zealot" : this.xmlLocations;
        XmlScanner.newInstance().scan(xmlLocations);
        TaggerScanner.newInstance().scan(handlerLocations);
    }

    /**
     * 扫描 xml文件所在的文件位置 并识别配置加载到内存缓存中.
     * @param xmlLocations zealot的XML文件所在的位置
     * @return ZealotConfigManager的全局唯一实例
     */
    public ZealotConfigManager initLoadXmlLocations(String xmlLocations) {
        this.xmlLocations = xmlLocations;
        this.xmlLocations = StringHelper.isBlank(this.xmlLocations) ? "zealot" : this.xmlLocations;
        XmlScanner.newInstance().scan(xmlLocations);
        this.cachingXmlAndEval();
        return this;
    }

    /**
     * 扫描 handler文件所在的文件位置 并识别配置加载到内存缓存中.
     * @param handlerLocations zealot的自定义handler处理器所在的位置
     * @return ZealotConfigManager的全局唯一实例
     */
    public ZealotConfigManager initLoadHandlerLocations(String handlerLocations) {
        this.handlerLocations = handlerLocations;
        TaggerScanner.newInstance().scan(handlerLocations);
        return this;
    }

    /**
     * 清空zealot所有缓存的内容.
     * 包括xml命名空间路径缓存、xml节点缓存
     */
    public void clear() {
        XmlContext.INSTANCE.getXmlPathMap().clear();
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

        log.info("Zealot加载器开始加载，Zealot配置类为:" + configClass);
        Object temp;
        try {
            temp = Class.forName(configClass).newInstance();
        } catch (Exception e) {
            throw new ConfigNotFoundException("初始化zealotConfig实例失败,配置名称为:" + configClass, e);
        }

        // 判断获取到的类是否是AbstractZealotConfig的子类，如果是，则加载xml和自定义标签
        if (temp instanceof AbstractZealotConfig) {
            this.loadZealotConfig((AbstractZealotConfig) temp);
        }
    }

    /**
     * 加载初始化zealotConfig的子类信息，并执行初始化zealot配置信息到缓存中.
     * @param zealotConfig 配置类
     */
    private void loadZealotConfig(AbstractZealotConfig zealotConfig) {
        zealotConfig.configNormal(NormalConfig.getInstance());
        zealotConfig.configXml(XmlContext.INSTANCE);
        zealotConfig.configTagHandler();
        log.warn("Zealot的配置信息加载完成!");
    }

    /**
     * 缓存xml、打印bannner并做初次计算.
     */
    private void cachingXmlAndEval() {
        this.cachingXmlZealots();
        BannerLoader.newInstance().print(NormalConfig.getInstance().isPrintBanner());
        this.testFirstEvaluate();
    }

    /**
     * 将每个zealotxml配置文件的key和文档缓存到ConcurrentHashMap内存缓存中.
     */
    @SuppressWarnings("unchecked")
    private void cachingXmlZealots() {
        Map<String, String> xmlMaps = XmlContext.INSTANCE.getXmlPathMap();

        // 遍历所有的xml文档，将每个zealot节点缓存到ConcurrentHashMap内存缓存中
        for (Map.Entry<String, String> entry: xmlMaps.entrySet()) {
            String nameSpace = entry.getKey();
            String filePath = entry.getValue();

            // 根据文件路径获取对应的dom4j Document对象.
            Document doc = XmlNodeHelper.getDocument(filePath);
            if (doc == null) {
                throw new ConfigNotFoundException("注意：未找到配置文件中xml对应的dom4j Document文档,nameSpace为:" + nameSpace);
            }

            // 获取该文档下所有的zealot元素,
            List<Node> zealotNodes = doc.selectNodes(ZealotConst.ZEALOT_TAG);
            for (Node zealotNode: zealotNodes) {
                String zealotId = XmlNodeHelper.getNodeText(zealotNode.selectSingleNode(ZealotConst.ATTR_ID));
                if (StringHelper.isBlank(zealotId)) {
                    throw new NodeNotFoundException("该xml文件中有zealot节点的zealotId属性为空，请检查！文件为:" + filePath);
                }

                // zealot节点缓存到Map中，key是由nameSpace和zealot id组成,用"@@"符号分隔,value是zealotNode
                String zealotKey = StringHelper.concat(nameSpace, ZealotConst.SP_AT, zealotId);
                AbstractZealotConfig.getZealots().put(zealotKey, zealotNode);
            }
        }
    }

    /**
     * 测试第一次MVEL表达式的计算,会缓存MVEL相关准备工作，加快后续的MVEL执行.
     */
    private void testFirstEvaluate() {
        Map<String, Object> context = new HashMap<String, Object>(4);
        context.put("foo", "hello");
        ParseHelper.parseTemplate("@if{?foo != empty}Hello World!@end{}", context);
        ParseHelper.parseExpressWithException("foo != empty", context);
    }

}