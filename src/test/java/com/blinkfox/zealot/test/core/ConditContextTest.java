package com.blinkfox.zealot.test.core;

import com.blinkfox.zealot.test.bean.BuildSource;
import com.blinkfox.zealot.test.bean.SqlInfo;
import com.blinkfox.zealot.test.config.MyZealotConfig;
import com.blinkfox.zealot.test.config.ZealotConfigManager;
import com.blinkfox.zealot.test.exception.NodeNotFoundException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ConditContext测试类.
 * Created by blinkfox on 2017/4/24.
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
        BuildSource source = new BuildSource(SqlInfo.newInstance());
        ConditContext.buildSqlInfo(source, "abcd");
    }

}