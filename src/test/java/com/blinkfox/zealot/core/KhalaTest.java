package com.blinkfox.zealot.core;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.blinkfox.zealot.log.Log;

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
		String expectedSql = "SELECT u.id, u.name, u.email, ud.addr FROM user as u LEFT JOIN user_detail as ud ON u.id = ud.user_id WHERE u.name like ? AND u.email like ? GROUP BY u.id desc";
		assertEquals(expectedSql, sql);
	}

}