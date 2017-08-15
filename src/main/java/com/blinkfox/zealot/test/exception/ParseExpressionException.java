package com.blinkfox.zealot.test.exception;

/**
 * 解析表达式异常.
 * Created by blinkfox on 2017-03-30.
 */
public class ParseExpressionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息和异常信息组成的构造方法.
     * @param msg 日志消息
     * @param t Throwable对象
     */
    public ParseExpressionException(String msg, Throwable t) {
        super(msg, t);
    }

}