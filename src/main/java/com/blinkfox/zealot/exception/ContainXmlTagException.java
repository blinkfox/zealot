package com.blinkfox.zealot.exception;

/**
 * 包含了xml标签的异常.
 * @author blinkfox on 2017/4/16.
 */
public class ContainXmlTagException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息参数的构造方法.
     * @param msg 日志消息
     */
    public ContainXmlTagException(String msg) {
        super(msg);
    }

}