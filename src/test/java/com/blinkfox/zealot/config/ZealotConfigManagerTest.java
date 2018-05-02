package com.blinkfox.zealot.config;

import com.blinkfox.zealot.config.entity.TagHandler;
import com.blinkfox.zealot.config.entity.XmlContext;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * ZealotConfigManager单元测试.
 *
 * @author blinkfox on 2018-04-30.
 */
public class ZealotConfigManagerTest {

    private static final String XML_LOCATIONS = "zealot";

    private static final String HANDLER_LOCATIONS = "com.blinkfox.zealot.test.handler";

    /**
     * 测试扫描和配置xml文件.
     */
    @Test
    public void initLoadLocations() {
        ZealotConfigManager zealotConfigManager = ZealotConfigManager.getInstance()
                .initLoadXmlLocations(XML_LOCATIONS)
                .initLoadHandlerLocations(HANDLER_LOCATIONS);

        Assert.assertEquals(XML_LOCATIONS, zealotConfigManager.getXmlLocations());
        Assert.assertEquals(HANDLER_LOCATIONS, zealotConfigManager.getHandlerLocations());

        Map<String, String> xmlMaps = XmlContext.INSTANCE.getXmlPathMap();
        Assert.assertTrue(xmlMaps.size() >= 2);

        Map<String, TagHandler> tagHandlerMap = AbstractZealotConfig.getTagHandlerMap();
        Assert.assertTrue(tagHandlerMap.size() >= 46);
    }

}