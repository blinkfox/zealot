package com.blinkfox.zealot.exception;

/**
 * XML解析出错异常.
 *
 * @author blinkfox on 2018-4-23.
 */
public class XmlParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息参数的构造方法.
     * @param msg 日志消息
     * @param t Throwable对象
     */
    public XmlParseException(String msg, Throwable t) {
        super(msg, t);
    }
}
