package com.blinkfox.zealot.test.helpers;

import com.blinkfox.zealot.helpers.IoHelper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * IoHelper的单元测试类.
 * Created by blinkfox on 2017/4/29.
 */
public class IoHelperTest {

    /**
     * 测试是否是XML文件.
     */
    @Test
    public void testIsXmlFile() {
        Assert.assertFalse(IoHelper.isXmlFile(null));
        Assert.assertFalse(IoHelper.isXmlFile("ab/.xmls"));
        Assert.assertTrue(IoHelper.isXmlFile("zealot/ab.xml"));
    }

    /**
     * 测试关闭流的方法.
     */
    @Test(expected = IOException.class)
    public void testCloseQuietly() throws FileNotFoundException {
        IoHelper.closeQuietly(null);
        IoHelper.closeQuietly(new FileReader(""));
    }

}