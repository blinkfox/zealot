package com.blinkfox.zealot.test;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.MyZealotConfig;
import com.blinkfox.zealot.core.Zealot;
import com.blinkfox.zealot.loader.ZealotConfigManager;
import com.blinkfox.zealot.log.Log;

/**
 * Zealot单元测试类
 * @author blinkfox
 * @date 2016-12-26
 */
public class ZealotTest {

    private static final Log log = Log.get(ZealotTest.class);

    // 自定义的配置类路径
    private static final String CONFIG_CLASS = "com.blinkfox.zealot.config.MyZealotConfig";

    /**
     * 初始化zealot信息到缓存中
     */
    @BeforeClass
    public static void before() {
        ZealotConfigManager.getInstance().initLoad(CONFIG_CLASS);
        log.info("加载Zealot缓存信息成功!");
    }

    /**
     * 清空缓存
     */
    @AfterClass
    public static void after() {
        ZealotConfigManager.getInstance().clear();
        log.info("清空了Zealot缓存!");
    }

    /**
     * 测试根据用户ID生成SqlInfo信息的方法
     */
    @Test
    public void testGetUserById() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", "2");

        long startTime = System.currentTimeMillis();
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserById", paramMap);
        log.info("---生成sql耗时为:" + (System.currentTimeMillis() - startTime) + " ms");

        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();
        log.info("testGetUserById测试方法生成sql耗时为:" + sql);
        log.info("----testGetUserById测试方法的params:" + Arrays.toString(params));
        assertNotNull("sql不为空", sql);
    }

    /**
     * 测试根据多条件动态生成SqlInfo信息的方法
     */
    @Test
    public void testGetUsers() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("nickName", "张");
        paramMap.put("startAge", 23);
        paramMap.put("endAge", 28);
        paramMap.put("startBirthday", "1990-01-01 00:00:00");
        paramMap.put("endBirthday", "1991-01-01 23:59:59");
        paramMap.put("sexs", new Integer[]{0, 1});

        long startTime = System.currentTimeMillis();
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserInfo", paramMap);
        log.info("testGetUsers测试方法生成sql耗时为:" + (System.currentTimeMillis() - startTime) + " ms");

        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();
        log.info("testGetUsers测试方法的sql:" + sql);
        log.info("testGetUsers测试方法的params:" + Arrays.toString(params));
        assertNotNull("sql不为空", sql);
    }

}