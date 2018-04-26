package com.blinkfox.zealot.config;

import com.blinkfox.zealot.config.entity.XmlContext;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * XmlScanConfigureTest.
 *
 * @author blinkfox on 2018/4/24.
 */
public class XmlScannerTest {

    private XmlScanner xmlScanner;

    /**
     * 构造实例.
     */
    @Before
    public void init() {
        this.xmlScanner = XmlScanner.newInstance();
    }

    /**
     * 销毁实例.
     */
    @After
    public void destroy() {
        this.xmlScanner = null;
    }

    /**
     * 测试扫描配置的包下的所有xml文件.
     */
    @Test
    public void scan() {
        String xmlLocations = "zealot, zealot/user   ,  zealot/student, zealot/student.xml";
        this.xmlScanner.scan(xmlLocations);

        Map<String, String> xmlMap = XmlContext.INSTANCE.getXmlPathMap();
        Assert.assertNotNull(xmlMap);
    }

}