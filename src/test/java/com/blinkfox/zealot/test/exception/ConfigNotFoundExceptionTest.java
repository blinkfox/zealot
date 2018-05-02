package com.blinkfox.zealot.test.exception;

import com.blinkfox.zealot.exception.ConfigNotFoundException;

import org.junit.Test;

/**
 * ConfigNotFoundExceptio的单元测试类.
 * @author blinkfox on 2017/4/29.
 */
public class ConfigNotFoundExceptionTest {

    /**
     * 测试构造方法.
     */
    @Test(expected = ConfigNotFoundException.class)
    public void testConfigNotFoundException() {
        throw new ConfigNotFoundException("配置未找到异常");
    }

    /**
     * 测试构造方法2.
     */
    @Test(expected = ConfigNotFoundException.class)
    public void testConfigNotFoundException2() {
        throw new ConfigNotFoundException("配置未找到异常", null);
    }

}