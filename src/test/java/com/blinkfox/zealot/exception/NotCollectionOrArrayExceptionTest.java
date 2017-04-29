package com.blinkfox.zealot.exception;

import org.junit.Test;

/**
 * NotCollectionOrArrayException单元测试类.
 * Created by blinkfox on 2017/4/29.
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