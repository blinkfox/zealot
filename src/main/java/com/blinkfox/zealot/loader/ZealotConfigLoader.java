package com.blinkfox.zealot.loader;

import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.dom4j.Document;
import com.blinkfox.zealot.bean.XmlContext;
import com.blinkfox.zealot.config.AbstractZealotConfig;
import com.blinkfox.zealot.helpers.Dom4jHelper;

/**
 * Zealot配置的servlet监听器的初始化加载类
 * Created by blinkfox on 2016/10/30.
 */
public class ZealotConfigLoader implements ServletContextListener {
	
	// zealot配置类对象
    private AbstractZealotConfig zealotConfig;

    // zealotConfig对应的类全路径常量字符串
    private static final String CONFIG_CLASS = "zealotConfigClass";

    /**
     * ZealotConfig销毁时执行的方法
     * @param arg0
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    	zealotConfig = null;
    }

    /**
     * 应用服务器启动时执行
     * @param event
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("----------Zealot应用程序监听器开始启动");

        // 创建XmlContext对象和创建执行ZealotConfig中的配置
        XmlContext xmlContext = XmlContext.getInstance();
        createZealotConfig(event, xmlContext);

        // 获取遍历每个zealotxml配置文件，将其key和文档缓存到ConcurrentHashMap内存缓存中
        cachingXmlZealots();
    }

    /**
     * 初始化zealotConfig的之类，并执行初始化mapper到缓存中
     * @param event
     * @param xmlContext
     */
    private void createZealotConfig(ServletContextEvent event, XmlContext xmlContext) {
        String configClass = event.getServletContext().getInitParameter(CONFIG_CLASS);
        System.out.println("----------启动得到的参数name:" + configClass);
        if (configClass == null) {
            throw new RuntimeException("请在web.xml设置zealotConfigClass参数");
        }

        Object temp;
        try {
            temp = Class.forName(configClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("不能创建zealotConfig实例: " + configClass, e);
        }

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
        for (Iterator<Map.Entry<String, String>> it = xmlMaps.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            Document document = Dom4jHelper.getDocument(value);
            if (document != null) {
            	AbstractZealotConfig.getZealots().put(key, document);
            }
        }
    }
	
}