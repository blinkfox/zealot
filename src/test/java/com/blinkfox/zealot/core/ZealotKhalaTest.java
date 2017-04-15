package com.blinkfox.zealot.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.core.inters.ICustomAction;
import com.blinkfox.zealot.log.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ZealotKhala的单元测试类.
 * 下面的拼接程序不一定是正确可执行的SQL，仅仅用来测试程序.
 * Created by blinkfox on 2017-03-31.
 */
public class ZealotKhalaTest {

    private static Map<String, Object> context;

    private static final Log log = Log.get(ZealotKhalaTest.class);

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
        SqlInfo sqlInfo = ZealotKhala.start().end();
        assertEquals("", sqlInfo.getJoin().toString());
        assertArrayEquals(new Object[]{}, sqlInfo.getParamsArr());
    }

    /**
     * 测试INSERT的一些拼接方式.
     */
    @Test
    public void testInsert() {
        SqlInfo sqlInfo = ZealotKhala.start()
                .insertInto("user").values("('3', 'lisi')")
                .end();
        String sql = sqlInfo.getSql();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("INSERT INTO user VALUES ('3', 'lisi')", sql);
        assertArrayEquals(new Object[]{}, arr);
        log.info("testDelete()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

    /**
     * 测试DELETE的一些拼接方式.
     */
    @Test
    public void testDelete() {
        SqlInfo sqlInfo = ZealotKhala.start()
                .deleteFrom("user")
                .end();
        String sql = sqlInfo.getSql();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("DELETE FROM user", sql);
        assertArrayEquals(new Object[]{}, arr);
        log.info("testDelete()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

    /**
     * 测试UPDATE的一些拼接方式.
     */
    @Test
    public void testUpdate() {
        SqlInfo sqlInfo = ZealotKhala.start()
                .update("user").set("nick_name = 'wangwu'")
                .end();
        String sql = sqlInfo.getSql();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("UPDATE user SET nick_name = 'wangwu'", sql);
        assertArrayEquals(new Object[]{}, arr);
        log.info("testUpdate()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

    /**
     * 测试SELECT的一些拼接方式.
     */
    @Test
    public void testSelect() {
        SqlInfo sqlInfo = ZealotKhala.start()
                .select("u.id, u.nick_name, u.email")
                .from("user").as("u")
                .innerJoin("corp as c").on("u.corp_id = c.id")
                .leftJoin("dept").as("d").on("u.dept_id = d.id")
                .rightJoin("office").as("o").on("u.office_id = o.id")
                .fullJoin("user_detail").as("ud").on("u.detail_id = ud.id")
                // .where("u.id = ?").param("3")
                .where("u.id = ?", "3")
                .and("u.nick_name like '%zhang%'")
                .when(true, "AND u.email = ?", "san@163.com")
                .doAnything(true, new ICustomAction() {
                    @Override
                    public void execute(StringBuilder join, List<Object> params) {
                        join.append(" abc111");
                        params.add(5);
                        log.info("执行了自定义操作，可拼接字符串和有序参数...");
                    }
                })
                .groupBy("u.id").having("u.id")
                .orderBy("u.id").desc().text(", u.nick_name", "zhang").asc()
                .unionAll()
                .select("u.id, u.nick_name, u.email")
                .from("user2")
                .limit("5")
                .offset("3")
                .end();
        String sql = sqlInfo.getSql();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("SELECT u.id, u.nick_name, u.email FROM user AS u INNER JOIN corp as c ON u.corp_id = c.id "
                + "LEFT JOIN dept AS d ON u.dept_id = d.id RIGHT JOIN office AS o ON u.office_id = o.id "
                + "FULL JOIN user_detail AS ud ON u.detail_id = ud.id WHERE u.id = ? AND u.nick_name "
                + "like '%zhang%' AND u.email = ? abc111 GROUP BY u.id HAVING u.id ORDER BY u.id DESC , u.nick_name ASC "
                + "UNION ALL SELECT u.id, u.nick_name, u.email FROM user2 LIMIT 5 OFFSET 3", sql);
        assertArrayEquals(new Object[]{"3", "san@163.com", 5, "zhang"}, arr);
        log.info("testNormal()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

    /**
     * any任何文字和参数的相关方法测试.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testAny() {
        List<String> citys = (List<String>) context.get("citys");
        SqlInfo sqlInfo = ZealotKhala.start()
                .text("select u.id, u.nick_name from user as u where ")
                .text("u.id = ? ").param(5)
                .text("and u.nick_name like ? ").param("lisi")
                .text("and u.sex in (?, ?, ?, ?, ?) ").param(0, 1).param(2, 3, 4)
                .text("and u.city in (?, ?, ?) ").param(citys)
                .end();
        String sql = sqlInfo.getSql();
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
        SqlInfo sqlInfo = ZealotKhala.start()
                .equal("u.id", context.get("id"), "4".equals(context.get("id")))
                .equal("u.nick_name", context.get("name"))
                .equal("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andEqual("u.age", context.get("myAge"))
                .andEqual("u.true_age", context.get("myAge"))
                .andEqual("u.true_age", context.get("myAge"), context.get("myAge") != null)
                .andEqual("u.email", context.get("myAge"), context.get("myEmail") == null)
                .equal("u.nick_name", context.get("name"))
                .orEqual("u.email", context.get("myEmail"))
                .orEqual("u.birthday", context.get("myBirthday"))
                .orEqual("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orEqual("u.nick_name", context.get("myBirthday"), context.get("name") == null)
                .equal("u.id", context.get("id"))
                .end();
        log.info("testEqual()方法执行耗时:" + (System.currentTimeMillis() - start) + " ms");
        String sql = sqlInfo.getSql();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("u.nick_name = ? u.email = ? AND u.age = ? AND u.true_age = ? AND u.true_age = ? "
                + "u.nick_name = ? OR u.email = ? OR u.birthday = ? OR u.birthday = ? u.id = ?", sql);
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
        SqlInfo sqlInfo = ZealotKhala.start()
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
        String sql = sqlInfo.getSql();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("u.nick_name LIKE ? u.email LIKE ? AND u.age LIKE ? AND u.true_age LIKE ? "
                + "AND u.true_age LIKE ? u.nick_name LIKE ? OR u.email LIKE ? OR u.birthday LIKE ? "
                + "OR u.birthday LIKE ? u.id LIKE ?", sql);
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
        SqlInfo sqlInfo = ZealotKhala.start()
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
        String sql = sqlInfo.getSql();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("u.age BETWEEN ? AND ? u.birthday <= ? AND u.age BETWEEN ? AND ? AND u.age BETWEEN ? AND ? "
                + "AND u.birthday <= ? OR u.age BETWEEN ? AND ? OR u.age BETWEEN ? AND ? OR u.birthday <= ?", sql);
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

        SqlInfo sqlInfo = ZealotKhala.start()
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
        String sql = sqlInfo.getSql();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("u.sex in (?, ?) u.city in (?, ?, ?) u.sex in (?, ?) AND u.sex in (?, ?) "
                + "AND u.city in (?, ?, ?) AND u.sex in (?, ?) OR u.sex in (?, ?) OR u.city in (?, ?, ?) "
                + "OR u.sex in (?, ?)", sql);
        assertArrayEquals(new Object[]{0, 1, "四川", "北京", "上海", 0, 1, 0, 1, "四川", "北京", "上海", 0, 1,
                0, 1, "四川", "北京", "上海", 0, 1} ,arr);
        log.info("testIn()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

    /**
     * 测试使用ZealotKhala书写的sql.
     */
    @Test
    public void testSql() {
        String userName = "zhang";
        String startBirthday = "1990-03-25";
        String endBirthday = "2010-08-28";

        SqlInfo sqlInfo = ZealotKhala.start()
                .select("u.id, u.name, u.email, d.birthday, d.address")
                .from("user AS u")
                .leftJoin("user_detail AS d").on("u.id = d.user_id")
                .where("u.id != ''")
                .andLike("u.name", userName, userName != null)
                .doAnything(true, new ICustomAction() {
                    @Override
                    public void execute(final StringBuilder join, final List<Object> params) {
                        join.append("abc111");
                        params.add(5);
                        log.info("执行了自定义操作，可任意拼接字符串和有序参数...");
                    }
                })
                .andGreaterThan("u.age", 21)
                .andLessThan("u.age", 13)
                .andGreaterEqual("d.birthday", startBirthday)
                .andLessEqual("d.birthday", endBirthday)
                .andBetween("d.birthday", startBirthday, endBirthday)
                .orderBy("d.birthday").desc()
                .end();
        String sql = sqlInfo.getSql();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("SELECT u.id, u.name, u.email, d.birthday, d.address FROM user AS u "
                + "LEFT JOIN user_detail AS d ON u.id = d.user_id WHERE u.id != '' AND u.name LIKE ? "
                + "abc111 AND u.age > ? AND u.age < ? AND d.birthday >= ? AND d.birthday <= ? "
                + "AND d.birthday BETWEEN ? AND ? ORDER BY d.birthday DESC", sql);
        assertArrayEquals(new Object[]{"%zhang%", 5, 21, 13, "1990-03-25", "2010-08-28",
                "1990-03-25", "2010-08-28"} ,arr);
        log.info("testSql()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

}