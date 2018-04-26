package com.blinkfox.zealot.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * TaggerScanner的单元测试类.
 *
 * @author blinkfox on 2018-04-26.
 */
public class TaggerScannerTest {

    private TaggerScanner taggerScanner;

    /**
     * 构造实例.
     */
    @Before
    public void init() {
        this.taggerScanner = TaggerScanner.newInstance();
    }

    /**
     * 销毁实例.
     */
    @After
    public void destroy() {
        this.taggerScanner = null;
    }

    /**
     * 测试扫描配置的包下的所有xml文件.
     */
    @Test
    public void scan() {
        String locations = "com.blinkfox.zealot, com.blinkfox.zealot.bean.SqlInfo.java, "
                + "com.blinkfox.bean.BuildSource.class";
        this.taggerScanner.scan(locations);
    }
}