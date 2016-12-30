package com.blinkfox.zealot.core;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.MyZealotConfig;
import com.blinkfox.zealot.log.Log;

/**
 * Zealot单元测试类
 * Created by blinkfox on 2016/12/26.
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
        // 构造查询的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", "2");

        // 生成sql，并得到和打印对应的sql和参数
        long startTime = System.currentTimeMillis();
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserById", paramMap);
        log.info("testGetUserById测试方法生成的sql耗时:" + (System.currentTimeMillis() - startTime) + " ms");
        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();
        log.info("testGetUserById测试方法生成的sql为:" + sql);
        log.info("----testGetUserById测试方法的params:" + Arrays.toString(params));

        // 测试结果断言
        String expectedSql = "select * from user where id = ?";
        Object[] expectedParams = new Object[]{"2"};
        assertEquals(expectedSql, sql);
        assertArrayEquals(expectedParams, params);
    }

    /**
     * 测试根据多条件动态生成SqlInfo信息的方法
     */
    @Test
    public void testGetUsers() {
        // 构造查询的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("nickName", "张");
        paramMap.put("startAge", 23);
        paramMap.put("endAge", 28);
        paramMap.put("startBirthday", "1990-01-01 00:00:00");
        paramMap.put("endBirthday", "1991-01-01 23:59:59");
        paramMap.put("sexs", new Integer[]{0, 1});

        // 生成sql，并得到和打印对应的sql和参数
        long startTime = System.currentTimeMillis();
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserInfo", paramMap);
        log.info("testGetUsers测试方法生成的sql耗时:" + (System.currentTimeMillis() - startTime) + " ms");
        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();
        log.info("testGetUsers测试方法的sql:" + sql);
        log.info("testGetUsers测试方法的params:" + Arrays.toString(params));

        // 测试结果断言
        String expectedSql = "select * from user where nickname LIKE ? AND age BETWEEN ? AND ? " +
                "AND birthday BETWEEN ? AND ? AND sex in (?, ?) order by id desc";
        Object[] expectedParams = new Object[]{"%张%", 23, 28, "1990-01-01 00:00:00",
                "1991-01-01 23:59:59", 0, 1};
        assertEquals(expectedSql, sql);
        assertArrayEquals(expectedParams, params);
    }

    /**
     * 测试根据流程控制标签来动态生成SqlInfo信息的方法
     */
    @Test
    public void testGetUsersByFlowTag() {
        // 构造查询的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("nickName", "张");
        paramMap.put("email", "zhang");

        // 生成sql，并得到和打印对应的sql和参数
        long startTime = System.currentTimeMillis();
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUsersByFlowTag", paramMap);
        log.info("testGetUsersByFlowTag 生成的sql耗时:" + (System.currentTimeMillis() - startTime) + " ms");
        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();
        log.info("testGetUsersByFlowTag 测试方法的sql:" + sql);
        log.info("testGetUsersByFlowTag 测试方法的params:" + Arrays.toString(params));

        // 测试结果断言
        String expectedSql = "select * from user where nickname LIKE ? AND email like '%zhang%' " +
                "order by id desc";
        Object[] expectedParams = new Object[]{"%张%"};
        assertEquals(expectedSql, sql);
        assertArrayEquals(expectedParams, params);
    }
    
    /**
     * 测试根据自定义标签来动态生成SqlInfo信息的方法
     */
    @Test
    public void testGetUsersByCustomTag() {
        // 构造查询的参数
    	Map<String, Object> user = new HashMap<String, Object>();
        user.put("userId", 3);
        user.put("userEmail", "san");

        // 生成sql，并得到和打印对应的sql和参数
        long startTime = System.currentTimeMillis();
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserWithIdEmail", user);
        log.info("testGetUsersByCustomTag 生成的sql耗时:" + (System.currentTimeMillis() - startTime) + " ms");
        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();
        log.info("testGetUsersByCustomTag 测试方法的sql:" + sql);
        log.info("testGetUsersByCustomTag 测试方法的params:" + Arrays.toString(params));

        // 测试结果断言
        String expectedSql = "select * from user where id = ?";
        Object[] expectedParams = new Object[]{3};
        assertEquals(expectedSql, sql);
        assertArrayEquals(expectedParams, params);
    }

}