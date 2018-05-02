package com.blinkfox.zealot.test.exception;

import com.blinkfox.zealot.exception.NotCollectionOrArrayException;

import org.junit.Test;

/**
 * NotCollectionOrArrayException单元测试类.
 * @author blinkfox on 2017/4/29.
 */
public class NotCollectionOrArrayExceptionTest {

    /**
     * 测试不是集合或数组的异常.
     */
    @Test(expected = NotCollectionOrArrayException.class)
    public void testNotCollectionOrArrayException() {
        throw new NotCollectionOrArrayException("不是集合或数组的异常.");
    }

}