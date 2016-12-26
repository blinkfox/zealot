package com.blinkfox.zealot.loader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.exception.ConfigNotFoundException;
import com.blinkfox.zealot.exception.NodeNotFoundException;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.log.Log;
import org.dom4j.Document;
import com.blinkfox.zealot.bean.XmlContext;
import com.blinkfox.zealot.config.AbstractZealotConfig;
import com.blinkfox.zealot.helpers.XmlNodeHelper;
import org.dom4j.Node;

/**
 * Zealot配置的servlet监听器的初始化加载类
 * Created by blinkfox on 2016/10/30.
 */
public class ZealotConfigLoader implements ServletContextListener {

    // 得到 log 实例
    private static final Log log = Log.get(ZealotConfigLoader.class);
	
	// zealot配置类对象
    private AbstractZealotConfig zealotConfig;

    // zealotConfig对应的类全路径常量字符串
    private static final String CONFIG_CLASS = "zealotConfigClass";

    /**
     * ZealotConfig销毁时执行的方法
     * @param arg0 上下文事件对象
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    	zealotConfig = null;
    }

    /**
     * 应用服务器启动时执行
     * @param event 上下文事件对象
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        // 创建XmlContext对象和创建执行ZealotConfig中的配置
        XmlContext xmlContext = XmlContext.getInstance();
        createZealotConfig(event, xmlContext);

        // 获取遍历每个zealotxml配置文件，将其key和文档缓存到ConcurrentHashMap内存缓存中
        cachingXmlZealots();
    }

    /**
     * 初始化zealotConfig的之类，并执行初始化mapper到缓存中
     * @param event 上下文事件对象
     * @param xmlContext xmlContext实例
     */
    private void createZealotConfig(ServletContextEvent event, XmlContext xmlContext) {
        String configClass = event.getServletContext().getInitParameter(CONFIG_CLASS);
        log.info("----zealot加载器开始加载，得到的 zealotConfigClass 参数值:" + configClass);
        if (configClass == null) {
            throw new ConfigNotFoundException("在 web.xml 中未设置 zealotConfigClass 参数");
        }

        Object temp;
        try {
            temp = Class.forName(configClass).newInstance();
        } catch (Exception e) {
            throw new ConfigNotFoundException("初始化zealotConfig实例失败,配置名称为:" + configClass, e);
        }

        // 判断获取到的类是否是AbstractZealotConfig的子类
        if (temp instanceof AbstractZealotConfig) {
            zealotConfig = (AbstractZealotConfig) temp;
            zealotConfig.configXml(xmlContext);
            zealotConfig.configTagHandler();
        }
    }

    /**
     * 将每个zealotxml配置文件的key和文档缓存到ConcurrentHashMap内存缓存中
     */
    private void cachingXmlZealots() {
        Map<String, String> xmlMaps = XmlContext.getXmlMap();

        // 遍历所有的xml文档，将每个zealot节点缓存到ConcurrentHashMap内存缓存中
        for (Iterator<Map.Entry<String, String>> it = xmlMaps.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = it.next();
            String nameSpace = entry.getKey();
            String value = entry.getValue();
            Document document = XmlNodeHelper.getDocument(value);
            if (document == null) {
                throw new ConfigNotFoundException("未找到zealot xml的配置文件，nameSpace为:" + nameSpace);
            }

            // 获取该文档下所有的zealot元素,
            @SuppressWarnings("unchecked")
			List<Node> zealotNodes = document.selectNodes(ZealotConst.ZEALOT_TAG);
            for (Node zealotNode: zealotNodes) {
                Node idNode = zealotNode.selectSingleNode(ZealotConst.ATTR_ID);
                String zealotId = XmlNodeHelper.getNodeText(idNode);
                if (StringHelper.isBlank(zealotId)) {
                    throw new NodeNotFoundException("未获取到zealot节点,zealotId为:" + zealotId);
                }

                // zealot节点缓存到Map中，key是由nameSpace和zealot id组成,用"@"符号分隔,value是zealotNode
                String zealotKey = StringHelper.concat(nameSpace, ZealotConst.SP_AT, zealotId);
                AbstractZealotConfig.getZealots().put(zealotKey, zealotNode);
            }
        }
    }
	
}