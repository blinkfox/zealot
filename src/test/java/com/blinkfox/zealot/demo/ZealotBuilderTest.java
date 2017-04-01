package com.blinkfox.zealot.demo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.core.ZealotBuilder;
import com.blinkfox.zealot.log.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ZealotBuilder的单元测试类.
 * Created by blinkfox on 2017-03-31.
 */
public class ZealotBuilderTest {

    private static Map<String, Object> context;

    private static final Log log = Log.get(ZealotBuilderTest.class);

    /**
     * 初始化.
     */
    @BeforeClass
    public static void init() {
        context = new HashMap<String, Object>();
        context.put("id", "3");
        context.put("name", "zhagnsan");
        context.put("myEmail", "zhagnsan@163.com");
        context.put("myAge", 25);
        context.put("startAge", 18);
        context.put("endAge", 26);
        context.put("myBirthday", "1990-03-31");
        context.put("startBirthday", null);
        context.put("endBirthday", "2010-05-28");
        context.put("sexs", new Integer[] {0, 1});
        context.put("citys", Arrays.asList("四川", "北京", "上海"));
    }

    /**
     * start方法.
     */
    @Test
    public void start() {
        SqlInfo sqlInfo = ZealotBuilder.start().end();
        assertEquals("", sqlInfo.getJoin().toString());
        assertArrayEquals(new Object[]{}, sqlInfo.getParamsArr());
    }

    /**
     * any任何文字和参数的相关方法测试.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testAny() {
        List<String> citys = (List<String>) context.get("citys");
        SqlInfo sqlInfo = ZealotBuilder.start()
                .anyText("select u.id, u.nick_name from user as u where ")
                .anyText("u.id = ? ").anyParam(5)
                .anyText("and u.nick_name like ? ").anyParam("lisi")
                .anyText("and u.sex in (?, ?, ?, ?, ?) ").anyParam(0, 1).anyParam(2, 3, 4)
                .anyText("and u.city in (?, ?, ?)").anyParam(citys)
                .end();
        String sql = sqlInfo.getJoin().toString();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("select u.id, u.nick_name from user as u where u.id = ? and u.nick_name like ? "
                + "and u.sex in (?, ?, ?, ?, ?) and u.city in (?, ?, ?)", sql);
        assertArrayEquals(new Object[]{5, "lisi", 0, 1, 2, 3, 4, "四川", "北京", "上海"}, arr);
        log.info("testAny方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

    /**
     * equal相关方法测试.
     */
    @Test
    public void testEqual() {
        long start = System.currentTimeMillis();
        SqlInfo sqlInfo = ZealotBuilder.start()
                .equalled("u.id", context.get("id"), "4".equals(context.get("id")))
                .equalled("u.nick_name", context.get("name"))
                .equalled("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andEqual("u.age", context.get("myAge"))
                .andEqual("u.true_age", context.get("myAge"))
                .andEqual("u.true_age", context.get("myAge"), context.get("myAge") != null)
                .andEqual("u.email", context.get("myAge"), context.get("myEmail") == null)
                .equalled("u.nick_name", context.get("name"))
                .orEqual("u.email", context.get("myEmail"))
                .orEqual("u.birthday", context.get("myBirthday"))
                .orEqual("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orEqual("u.nick_name", context.get("myBirthday"), context.get("name") == null)
                .equalled("u.id", context.get("id"))
                .end();
        log.info("testEqual()方法执行耗时:" + (System.currentTimeMillis() - start) + " ms");
        String sql = sqlInfo.getJoin().toString();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals(" u.nick_name = ?  u.email = ?  AND u.age = ?  AND u.true_age = ?  AND u.true_age = ?  "
                + "u.nick_name = ?  OR u.email = ?  OR u.birthday = ?  OR u.birthday = ?  u.id = ? ", sql);
        assertArrayEquals(new Object[]{"zhagnsan", "zhagnsan@163.com", 25, 25, 25, "zhagnsan", "zhagnsan@163.com",
                "1990-03-31", "1990-03-31", "3"}, arr);
        log.info("testEqual()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

    /**
     * equal相关方法测试.
     */
    @Test
    public void testLike() {
        long start = System.currentTimeMillis();
        SqlInfo sqlInfo = ZealotBuilder.start()
                .like("u.id", context.get("id"), "4".equals(context.get("id")))
                .like("u.nick_name", context.get("name"))
                .like("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andLike("u.age", context.get("myAge"))
                .andLike("u.true_age", context.get("myAge"))
                .andLike("u.true_age", context.get("myAge"), context.get("myAge") != null)
                .andLike("u.email", context.get("myAge"), context.get("myEmail") == null)
                .like("u.nick_name", context.get("name"))
                .orLike("u.email", context.get("myEmail"))
                .orLike("u.birthday", context.get("myBirthday"))
                .orLike("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orLike("u.nick_name", context.get("myBirthday"), context.get("name") == null)
                .like("u.id", context.get("id"))
                .end();
        log.info("testLike()方法执行耗时:" + (System.currentTimeMillis() - start) + " ms");
        String sql = sqlInfo.getJoin().toString();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals(" u.nick_name LIKE ?  u.email LIKE ?  AND u.age LIKE ?  AND u.true_age LIKE ?  "
                + "AND u.true_age LIKE ?  u.nick_name LIKE ?  OR u.email LIKE ?  OR u.birthday LIKE ?  "
                + "OR u.birthday LIKE ?  u.id LIKE ? ", sql);
        assertArrayEquals(new Object[]{"%zhagnsan%", "%zhagnsan@163.com%", "%25%", "%25%", "%25%", "%zhagnsan%",
                "%zhagnsan@163.com%", "%1990-03-31%", "%1990-03-31%", "%3%"}, arr);
        log.info("testLike()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

    /**
     * equal相关方法测试.
     */
    @Test
    public void testBetween() {
        Integer startAge = (Integer) context.get("startAge");
        Integer endAge = (Integer) context.get("endAge");
        String startBirthday = (String) context.get("startBirthday");
        String endBirthday = (String) context.get("endBirthday");

        long start = System.currentTimeMillis();
        SqlInfo sqlInfo = ZealotBuilder.start()
                .between("u.age", startAge, endAge)
                .between("u.age", startAge, endAge, startAge == null && endAge == null)
                .between("u.birthday", startBirthday, endBirthday)
                .between("u.birthday", startBirthday, endBirthday, startBirthday != null)
                .andBetween("u.age", startAge, endAge)
                .andBetween("u.age", startAge, endAge, startAge != null && endAge != null)
                .andBetween("u.birthday", startBirthday, endBirthday)
                .andBetween("u.birthday", startBirthday, endBirthday, startBirthday != null)
                .orBetween("u.age", startAge, endAge)
                .orBetween("u.age", startAge, endAge, startAge != null && endAge != null)
                .orBetween("u.birthday", startBirthday, endBirthday)
                .orBetween("u.birthday", startBirthday, endBirthday, startBirthday != null)
                .end();
        log.info("testBetween()方法执行耗时:" + (System.currentTimeMillis() - start) + " ms");
        String sql = sqlInfo.getJoin().toString();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals(" u.age BETWEEN ? AND ?  u.birthday <= ?  AND u.age BETWEEN ? AND ?  AND u.age BETWEEN ? AND ?  "
                + "AND u.birthday <= ?  OR u.age BETWEEN ? AND ?  OR u.age BETWEEN ? AND ?  OR u.birthday <= ? ", sql);
        assertArrayEquals(new Object[]{18, 26, "2010-05-28", 18, 26, 18, 26, "2010-05-28", 18, 26, 18, 26,
                "2010-05-28"}, arr);
        log.info("testBetween()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

    /**
     * in相关方法测试.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testIn() {
        Integer[] sexs = (Integer[]) context.get("sexs");
        List<String> citys = (List<String>) context.get("citys");
        long start = System.currentTimeMillis();

        SqlInfo sqlInfo = ZealotBuilder.start()
                .in("u.sex", sexs)
                .in("u.city", citys)
                .in("u.sex", sexs, sexs != null)
                .in("u.city", citys, citys == null)
                .andIn("u.sex", sexs)
                .andIn("u.city", citys)
                .andIn("u.sex", sexs, sexs != null)
                .andIn("u.city", citys, citys == null)
                .orIn("u.sex", sexs)
                .orIn("u.city", citys)
                .orIn("u.sex", sexs, sexs != null)
                .orIn("u.city", citys, citys == null)
                .end();

        log.info("testIn()方法执行耗时:" + (System.currentTimeMillis() - start) + " ms");
        String sql = sqlInfo.getJoin().toString();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals(" u.sex in (?, ?)  u.city in (?, ?, ?)  u.sex in (?, ?)  AND u.sex in (?, ?)  "
                + "AND u.city in (?, ?, ?)  AND u.sex in (?, ?)  OR u.sex in (?, ?)  OR u.city in (?, ?, ?)  "
                + "OR u.sex in (?, ?) ", sql);
        assertArrayEquals(new Object[]{0, 1, "四川", "北京", "上海", 0, 1, 0, 1, "四川", "北京", "上海", 0, 1,
                0, 1, "四川", "北京", "上海", 0, 1} ,arr);
        log.info("testIn()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

}