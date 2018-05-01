package com.blinkfox.zealot.config;

import com.blinkfox.zealot.config.entity.TagHandler;
import com.blinkfox.zealot.config.entity.XmlContext;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

/**
 * ZealotConfigManager单元测试.
 *
 * @author blinkfox on 2018-04-30.
 */
public class ZealotConfigManagerTest {

    /**
     * 测试扫描和配置xml文件.
     */
    @Test
    public void initLoadXmlLocations() {
        ZealotConfigManager.getInstance().initLoadXmlLocations("zealot");
        Map<String, String> xmlMaps = XmlContext.INSTANCE.getXmlPathMap();
        Assert.assertTrue(xmlMaps.size() >= 2);
    }

    /**
     * 测试扫描和配置handler文件.
     */
    @Test
    public void initLoadHandlerLocations() {
        ZealotConfigManager.getInstance().initLoadHandlerLocations("com.blinkfox.zealot.test.handler");
        Map<String, TagHandler> tagHandlerMap = AbstractZealotConfig.getTagHandlerMap();
        Assert.assertTrue(tagHandlerMap.size() >= 46);
    }

}