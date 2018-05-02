package com.blinkfox.zealot.test.core;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.ZealotConfigManager;
import com.blinkfox.zealot.core.ConditContext;
import com.blinkfox.zealot.exception.NodeNotFoundException;
import com.blinkfox.zealot.test.config.MyZealotConfig;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ConditContext测试类.
 * @author blinkfox on 2017/4/24.
 */
public class ConditContextTest {

    /**
     * 初始化方法.
     */
    @BeforeClass
    public static void init() {
        ZealotConfigManager.getInstance().initLoad(MyZealotConfig.class);
    }

    /**
     * 清空缓存.
     */
    @AfterClass
    public static void after() {
        ZealotConfigManager.getInstance().clear();
    }

    /**
     * 测试构建sql片段时未找到Xml中Node节点异常的方法.
     */
    @Test(expected = NodeNotFoundException.class)
    public void testBuildSqlInfo() {
        ConditContext.buildSqlInfo(new BuildSource(SqlInfo.newInstance()), "abcd");
    }

}