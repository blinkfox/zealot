package com.blinkfox.zealot.config;

import com.blinkfox.zealot.config.entity.TagHandler;
import com.blinkfox.zealot.config.scanner.TaggerScanner;
import com.blinkfox.zealot.core.IConditHandler;
import com.blinkfox.zealot.log.Log;
import com.blinkfox.zealot.test.handler.TaggerTestHandler;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * TaggerScanner的单元测试类.
 *
 * @author blinkfox on 2018-04-26.
 */
public class TaggerScannerTest {

    private static Log log = Log.get(TaggerScanner.class);

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
     * 测试class中的'isAssignableFrom()'方法.
     */
    @Test
    public void isAssignableFrom() {
        Assert.assertTrue(IConditHandler.class.isAssignableFrom(IConditHandler.class));
        Assert.assertFalse(TaggerTestHandler.class.isAssignableFrom(IConditHandler.class));
        Class<?>[] classes = TaggerTestHandler.class.getInterfaces();
        for (Class<?> cls: classes) {
            if (IConditHandler.class.isAssignableFrom(cls)) {
                log.info("该类是IConditHandler的子类:" + cls.getName());
            }
        }
    }

    /**
     * 测试扫描配置的包下的所有xml文件.
     */
    @Test
    public void scan() {
        String locations = "com.blinkfox.zealot, com.blinkfox.zealot.bean.SqlInfo.java, "
                + "com.blinkfox.bean.BuildSource.class";
        this.taggerScanner.scan(locations);

        Map<String, TagHandler> tagHandlerMap = AbstractZealotConfig.getTagHandlerMap();
        Assert.assertNotNull(tagHandlerMap);
        Assert.assertTrue(tagHandlerMap.containsKey("helloTagger"));
        Assert.assertTrue(tagHandlerMap.containsKey("hw"));
        log.info("扫描到的tagHandlerMap集合:" + tagHandlerMap.toString());
    }
}