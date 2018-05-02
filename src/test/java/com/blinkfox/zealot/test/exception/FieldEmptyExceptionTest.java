package com.blinkfox.zealot.test.exception;

import com.blinkfox.zealot.exception.FieldEmptyException;

import org.junit.Test;

/**
 * FieldEmptyException单元测试类.
 * @author blinkfox on 2017/4/29.
 */
public class FieldEmptyExceptionTest {

    /**
     * 测试xml标签属性字段未找到的异常.
     */
    @Test(expected = FieldEmptyException.class)
    public void testFieldEmptyException() {
        throw new FieldEmptyException("xml标签属性字段未找到的异常");
    }

}