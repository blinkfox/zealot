package com.blinkfox.zealot.exception;

import org.junit.Test;

/**
 * ValidFailException的单元测试类.
 *
 * @author blinkfox on 2018-4-22.
 */
public class ValidFailExceptionTest {

    /**
     * 测试自定义异常.
     */
    @Test(expected = ValidFailException.class)
    public void testException() {
        throw new ValidFailException("校验不通过异常!");
    }

}