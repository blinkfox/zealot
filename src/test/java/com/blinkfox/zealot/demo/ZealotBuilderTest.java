package com.blinkfox.zealot.demo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.core.ZealotBuilder;
import com.blinkfox.zealot.log.Log;
import java.util.Arrays;
import java.util.HashMap;
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
        context.put("myBirthday", "1990-03-31");
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
     * equal相关方法测试.
     */
    @Test
    public void equalled() {
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
        log.info("equal()方法执行耗时:" + (System.currentTimeMillis() - start) + " ms");
        String sql = sqlInfo.getJoin().toString();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals(" u.nick_name = ?  u.email = ?  AND u.age = ?  AND u.true_age = ?  AND u.true_age = ?  "
                + "u.nick_name = ?  OR u.email = ?  OR u.birthday = ?  OR u.birthday = ?  u.id = ? ", sql);
        assertArrayEquals(new Object[]{"zhagnsan", "zhagnsan@163.com", 25, 25, 25, "zhagnsan", "zhagnsan@163.com",
                "1990-03-31", "1990-03-31", "3"}, sqlInfo.getParamsArr());
        log.info("equal()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

}