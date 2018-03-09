package com.blinkfox.zealot.test.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.bean.ParamWrapper;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.ZealotConfigManager;
import com.blinkfox.zealot.core.Zealot;
import com.blinkfox.zealot.log.Log;
import com.blinkfox.zealot.test.bean.Teacher;
import com.blinkfox.zealot.test.config.MyZealotConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Zealot单元测试类.
 * Created by blinkfox on 2016/12/26.
 */
public class ZealotTest {

    private static final Log log = Log.get(ZealotTest.class);

    /**
     * 初始化zealot信息到缓存中.
     */
    @BeforeClass
    public static void before() {
        ZealotConfigManager.getInstance().initLoad(new MyZealotConfig());
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
        String expectedSql = "select * from user as u where u.id = ? AND u.age <> ? and u.name IN (?, ?) "
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
                + "AND u.birthday BETWEEN ? AND ? AND u.sex IN (?, ?) order by u.id desc";
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

    /**
     * 测试`include`标签的使用方法.
     */
    @Test
    public void testQueryStudents() {
        // 构造查询的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("stuName", "张");
        paramMap.put("age", 19);
        paramMap.put("sexs", new Integer[]{0, 1});
        paramMap.put("startBirthday", "1990-01-01 00:00:00");
        paramMap.put("endBirthday", "1991-01-01 23:59:59");

        // 往查询参数中设置teacher实例.
        Teacher teacher = new Teacher();
        teacher.setId("123456");
        teacher.setName("李");
        teacher.setAge(31);
        teacher.setStartBirthday("1982-05-01 00:00:00");
        teacher.setEndBirthday("1996-10-13 00:00:00");
        paramMap.put("teacher", teacher);

        // 生成sql，并得到和打印对应的sql和参数
        long startTime = System.currentTimeMillis();
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.STUDENT_ZEALOT, "queryStudents", paramMap);
        log.info("testQueryStudents 生成的sql耗时:" + (System.currentTimeMillis() - startTime) + " ms");
        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();
        log.info("testGetUsersByCustomTag 测试方法的sql:" + sql);
        log.info("testGetUsersByCustomTag 测试方法的params:" + Arrays.toString(params));

        // 测试结果断言
        String expectedSql = "SELECT * FROM t_student AS s INNER JOIN t_teacher AS t ON s.c_tid = t.c_id "
                + "WHERE s.n_valid = 1 AND s.c_name LIKE ? AND s.n_age >= ? AND s.d_birthday BETWEEN ? AND ? "
                + "AND t.c_id = ? AND t.c_name LIKE ? AND t.n_age <= ? AND t.d_birthday BETWEEN ? AND ? "
                + "AND s.n_sex IN (?, ?) ORDER BY s.c_id LIMIT 20 OFFSET 0";
        Object[] expectedParams = new Object[]{"%张%", 19, "1990-01-01 00:00:00", "1991-01-01 23:59:59",
                "123456", "%李%", 31, "1982-05-01 00:00:00", "1996-10-13 00:00:00", 0, 1};
        assertEquals(expectedSql, sql);
        assertArrayEquals(expectedParams, params);
    }

    /**
     * 测试使用`ParamWrapper`来构建参数上下文的方法.
     */
    @Test
    public void testQueryStudentById() {
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.STUDENT_ZEALOT, "queryStudentById",
                ParamWrapper.newInstance("stuId", "123").toMap());
        assertEquals("SELECT * FROM t_student AS s WHERE s.c_id = ?", sqlInfo.getSql());
        assertArrayEquals(new String[]{"123"}, sqlInfo.getParamsArr());
    }

    /**
     * 测试使用`choose`标签来构建sql片段.
     */
    @Test
    public void testQueryByChoose() {
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.STUDENT_ZEALOT, "queryByChoose",
                ParamWrapper.newInstance("sex", "1").put("stuId", "123").put("state", false).put("age", 3).toMap());
        assertEquals("UPDATE t_student SET s.c_sex = 'male' , s.c_status = 'no' , s.c_age = '幼年' "
                + "WHERE s.c_id = '123'", sqlInfo.getSql());
    }

}