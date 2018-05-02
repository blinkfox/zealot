package com.blinkfox.zealot.test.exception;

import com.blinkfox.zealot.exception.NodeNotFoundException;

import org.junit.Test;

/**
 * NodeNotFoundException单元测试类.
 * @author blinkfox on 2017/4/29.
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