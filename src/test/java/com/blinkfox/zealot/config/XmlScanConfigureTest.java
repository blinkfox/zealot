package com.blinkfox.zealot.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * XmlScanConfigureTest.
 *
 * @author blinkfox on 2018/4/24.
 */
public class XmlScanConfigureTest {

    private XmlScanConfigure xmlScanConfigure;

    /**
     * 构造实例.
     */
    @Before
    public void init() {
        this.xmlScanConfigure = XmlScanConfigure.newInstance();
    }

    /**
     * 销毁实例.
     */
    @After
    public void destroy() {
        this.xmlScanConfigure = null;
    }

    /**
     * 测试扫描配置的包下的所有xml文件.
     */
    @Test
    public void scan() {
        String xmlLocations = "zealot/user, zealot/teacher   ,  zealot/student, zealot/student.xml";
        this.xmlScanConfigure.scan(xmlLocations);
    }

}