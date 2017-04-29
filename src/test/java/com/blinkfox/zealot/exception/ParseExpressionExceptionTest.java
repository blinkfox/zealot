package com.blinkfox.zealot.exception;

import org.junit.Test;

/**
 * ParseExpressionException单元测试类.
 * Created by blinkfox on 2017/4/29.
 */
public class ParseExpressionExceptionTest {

    /**
     * 测试解析表达式异常.
     */
    @Test(expected = ParseExpressionException.class)
    public void testParseExpressionException() {
        throw new ParseExpressionException("解析表达式异常.", new Throwable());
    }

}