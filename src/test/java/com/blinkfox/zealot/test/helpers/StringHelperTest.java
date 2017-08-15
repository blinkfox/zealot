package com.blinkfox.zealot.test.helpers;

import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.helpers.StringHelper;

import org.junit.Test;

/**
 * StringHelper的单元测试类.
 * Created by blinkfox on 2017/4/29.
 */
public class StringHelperTest {

    /**
     * 判断字符串是否为空的单元测试类.
     */
    @Test
    public void testIsBlank() {
        assertEquals(true, StringHelper.isBlank(null));
        assertEquals(true, StringHelper.isBlank(""));
        assertEquals(true, StringHelper.isBlank(" "));
        assertEquals(false, StringHelper.isBlank(" a "));
    }

    /**
     * 判断字符串是否不为空的单元测试类.
     */
    @Test
    public void testIsNotBlank() {
        assertEquals(false, StringHelper.isNotBlank(null));
        assertEquals(false, StringHelper.isNotBlank(""));
        assertEquals(false, StringHelper.isNotBlank(" "));
        assertEquals(true, StringHelper.isNotBlank(" a "));
    }

}