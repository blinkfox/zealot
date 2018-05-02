package com.blinkfox.zealot.test.log;

import com.blinkfox.zealot.log.Log;

import org.junit.Test;

/**
 * Log日志单元测试类.
 * @author blinkfox on 2017/4/29.
 */
public class LogTest {

    private static final Log log = Log.get(LogTest.class);

    /**
     * 测试warn日志.
     */
    @Test
    public void testWarn() {
        log.warn("这是'warn'级别的日志信息!");
    }

    /**
     * 测试error日志.
     */
    @Test
    public void testError() {
        log.error("这是'error'级别的日志信息!");
    }

    /**
     * 测试error日志.
     */
    @Test
    public void testError2() {
        log.error("这是'error'且带异常信息级别的日志信息!", null);
    }

}