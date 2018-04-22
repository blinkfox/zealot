package com.blinkfox.zealot.exception;

/**
 * 校验不通过时的异常.
 *
 * Created by blinkfox on 2018-04-22.
 */
public class ValidFailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息参数的构造方法.
     * @param msg 日志消息
     */
    public ValidFailException(String msg) {
        super(msg);
    }

}