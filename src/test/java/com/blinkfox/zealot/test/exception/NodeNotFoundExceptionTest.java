package com.blinkfox.zealot.test.exception;

import org.junit.Test;

/**
 * NodeNotFoundException单元测试类.
 * Created by blinkfox on 2017/4/29.
 */
public class NodeNotFoundExceptionTest {

    /**
     * 测试XML节点未找到异常.
     */
    @Test(expected = NodeNotFoundException.class)
    public void testNodeNotFoundException() {
        throw new NodeNotFoundException("XML节点未找到异常");
    }

}