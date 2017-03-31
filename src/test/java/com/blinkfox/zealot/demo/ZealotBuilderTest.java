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
     * equalled方法.
     */
    @Test
    public void equalled() {
        SqlInfo sqlInfo = ZealotBuilder.start()
                .equalled("u.id", (String) context.get("id"), "4".equals(context.get("id")))
                .equalled("u.nick_name", (String) context.get("name"))
                .equalled("u.email", (String) context.get("myEmail"), context.get("myEmail") != null)
                .end();
        String sql = sqlInfo.getJoin().toString();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals(" u.nick_name = ?  u.email = ? ", sql);
        assertArrayEquals(new Object[]{"zhagnsan", "zhagnsan@163.com"}, sqlInfo.getParamsArr());
        log.info("equal()方法生成的sql信息:" + sql + ",参数为:" + Arrays.toString(arr));
    }

}