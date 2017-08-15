package com.blinkfox.zealot.loader;

import com.blinkfox.zealot.config.ZealotConfigManager;
import com.blinkfox.zealot.config.entity.XmlContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Zealot配置的servlet监听器的初始化加载类
 * Created by blinkfox on 2016/10/30.
 */
public class ZealotConfigLoader implements ServletContextListener {

    /** zealotConfig对应的类全路径常量字符串. */
    private static final String CONFIG_CLASS = "zealotConfigClass";

    /**
     * ZealotConfig销毁时执行的方法.
     * @param event 上下文事件对象
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        XmlContext.INSTANCE.getXmlPathMap().clear();
    }

    /**
     * 应用服务器启动时执行.
     * @param event 上下文事件对象
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        // 根据配置的zealotConfig路径加载配置文件信息到缓存中
        String configClass = event.getServletContext().getInitParameter(CONFIG_CLASS);
        ZealotConfigManager.getInstance().initLoad(configClass);
    }

}