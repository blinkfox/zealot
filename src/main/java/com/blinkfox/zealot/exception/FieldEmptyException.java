package com.blinkfox.zealot.exception;

/**
 * 字段为空的异常.
 * Created by blinkfox on 2016/11/7.
 */
public class FieldEmptyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 默认无参构造方法.
     */
    public FieldEmptyException() {
        super();
    }

    /**
     * 附带日志消息参数的构造方法.
     * @param msg 日志消息
     */
    public FieldEmptyException(String msg) {
        super(msg);
    }

}