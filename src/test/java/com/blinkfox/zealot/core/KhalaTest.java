package com.blinkfox.zealot.core;

import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.log.Log;
import org.junit.Test;

/**
 * Khala单元测试类.
 * Created by blinkfox on 2016/12/27.
 */
public class KhalaTest {

    private static final Log log = Log.get(KhalaTest.class);

    /**
     * 使用Khala拼接sql的测试方法.
     */
    @Test
    public void testBuild() {
        String sql = Khala.newInstance().start()
                .select("u.id, u.name, u.email, ud.addr")
                .from("user as u")
                .leftJoin("user_detail as ud").on("u.id = ud.user_id")
                .where("u.name like ?").and("u.email like ?")
                .groupBy("u.id desc")
                .end();

        log.info("使用Khala拼接的sql为:" + sql);
        String expectedSql = "SELECT u.id, u.name, u.email, ud.addr FROM user as u LEFT JOIN user_detail as ud "
                + "ON u.id = ud.user_id WHERE u.name like ? AND u.email like ? GROUP BY u.id desc";
        assertEquals(expectedSql, sql);
    }

    /**
     * 测试end()方法.
     */
    @Test
    public void testEnd() {
        String oldSql = "   SELECT name, email FROM user \n WHERE id = '2'   ";
        String sql = Khala.newInstance().start().add(oldSql).end();
        assertEquals("SELECT name, email FROM user WHERE id = '2'", sql);

        // 测试sql为null的情形
        String nullSql = Khala.newInstance().end();
        assertEquals("", nullSql);
    }

    /**
     * 测试select生成sql的方法.
     */
    @Test
    public void testSelect() {
        String sql = Khala.newInstance().start()
                .select("*")
                .from("user as u")
                .innerJoin("user_detail as ud").on("ud.id = u.id")
                .rightJoin("dept").as("d").on("u.dept_id = d.id")
                .fullJoin("corp").as("c").on("u.corp_id = c.id")
                .where("u.age > ?")
                .groupBy("u.dept")
                .having("u.money > ?")
                .orderBy("u.age DESC")
                .end();

        String expectedSql = "SELECT * FROM user as u INNER JOIN user_detail as ud ON ud.id = u.id "
                + "RIGHT JOIN dept AS d ON u.dept_id = d.id FULL JOIN corp AS c ON u.corp_id = c.id "
                + "WHERE u.age > ? GROUP BY u.dept HAVING u.money > ? ORDER BY u.age DESC";
        assertEquals(expectedSql, sql);
    }

    /**
     * 测试insert语句生成sql的方法.
     */
    @Test
    public void testInsert() {
        String sql = Khala.newInstance().start()
                .insertInto("user(name, email)")
                .values("(?, ?)")
                .end();

        String expectedSql = "INSERT INTO user(name, email) VALUES (?, ?)";
        assertEquals(expectedSql, sql);
    }

    /**
     * 测试update语句生成sql的方法.
     */
    @Test
    public void testUpdate() {
        String sql = Khala.newInstance().start()
                .update("user")
                .set("name = ?")
                .where("id = ?")
                .end();

        String expectedSql = "UPDATE user SET name = ? WHERE id = ?";
        assertEquals(expectedSql, sql);
    }

    /**
     * 测试update语句生成sql的方法.
     */
    @Test
    public void testDelete() {
        String sql = Khala.newInstance().start()
                .delete("FROM user")
                .where("id = ?")
                .end();

        String expectedSql = "DELETE FROM user WHERE id = ?";
        assertEquals(expectedSql, sql);
    }

}