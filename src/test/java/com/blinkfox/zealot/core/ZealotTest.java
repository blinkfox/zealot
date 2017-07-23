package com.blinkfox.zealot.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.MyZealotConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.blinkfox.zealot.config.ZealotConfigManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zealot单元测试类.
 * Created by blinkfox on 2016/12/26.
 */
public class ZealotTest {

    private static final Logger log = LoggerFactory.getLogger(ZealotTest.class);

    /**
     * 初始化zealot信息到缓存中.
     */
    @BeforeClass
    public static void before() {
        ZealotConfigManager.getInstance().initLoad(MyZealotConfig.class);
        log.info("加载Zealot缓存信息成功!");
    }

    /**
     * 清空缓存.
     */
    @AfterClass
    public static void after() {
        ZealotConfigManager.getInstance().clear();
        log.info("清空了Zealot缓存!");
    }

    /**
     * 测试根据用户ID生成SqlInfo信息的方法.
     */
    @Test
    public void testGetUserById() {
        // 构造查询的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", "2");
        paramMap.put("age", 21);
        paramMap.put("name1", "张三");
        paramMap.put("name2", "李思");
        paramMap.put("email", "hello@gmail.com");

        // 生成sql，并得到和打印对应的sql和参数
        long startTime = System.currentTimeMillis();
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserById", paramMap);
        log.info("testGetUserById测试方法生成的sql耗时:" + (System.currentTimeMillis() - startTime) + " ms");
        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();
        log.info("testGetUserById测试方法生成的sql为:" + sql);
        log.info("----testGetUserById测试方法的params:" + Arrays.toString(params));

        // 测试结果断言
        String expectedSql = "select * from user as u where u.id = ? AND u.age <> ? and u.name in (?, ?) "
                + "and u.email = ? AND u.email LIKE ?";
        Object[] expectedParams = new Object[]{"2", 21, "张三", "李思", "hello@gmail.com", "%hello@gmail.com%"};
        assertEquals(expectedSql, sql);
        assertArrayEquals(expectedParams, params);
    }

    /**
     * 测试根据多条件动态生成SqlInfo信息的方法.
     */
    @Test
    public void testGetUsers() {
        // 构造查询的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("nickName", "张");
        paramMap.put("state", 1);
        paramMap.put("startAge", 23);
        paramMap.put("endAge", 28);
        paramMap.put("startBirthday", "1990-01-01 00:00:00");
        paramMap.put("endBirthday", "1991-01-01 23:59:59");
        paramMap.put("sexs", new Integer[]{0, 1});

        // 生成sql，并得到和打印对应的sql和参数
        long startTime = System.currentTimeMillis();
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserInfos", paramMap);
        log.info("testGetUsers测试方法生成的sql耗时:" + (System.currentTimeMillis() - startTime) + " ms");
        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();
        log.info("testGetUsers测试方法的sql:" + sql);
        log.info("testGetUsers测试方法的params:" + Arrays.toString(params));

        // 测试结果断言
        String expectedSql = "select * from user as u where u.nickname LIKE ? AND u.status = ? and u.age > ? "
                + "AND u.age < ? and u.age >= ? AND u.age >= ? AND u.age <= ? AND u.age BETWEEN ? AND ? "
                + "AND u.birthday BETWEEN ? AND ? AND u.sex in (?, ?) order by u.id desc";
        Object[] expectedParams = new Object[]{"%张%", 1, 23, 28, 23, 23, 28, 23, 28, "1990-01-01 00:00:00",
                "1991-01-01 23:59:59", 0, 1};
        assertEquals(expectedSql, sql);
        assertArrayEquals(expectedParams, params);
    }

    /**
     * 测试根据流程控制标签来动态生成SqlInfo信息的方法.
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
        String expectedSql = "select * from user where nickname LIKE ? AND email like '%zhang%' "
                + "order by id desc";
        Object[] expectedParams = new Object[]{"%张%"};
        assertEquals(expectedSql, sql);
        assertArrayEquals(expectedParams, params);
    }
    
    /**
     * 测试根据自定义标签来动态生成SqlInfo信息的方法.
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