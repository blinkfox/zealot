package com.blinkfox.zealot.exception;

/**
 * 自定义的Zealot运行时异常.
 *
 * @author blinkfox on 2018-04-24.
 */
public class ZealotException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息参数的构造方法.
     * @param msg 日志消息
     */
    public ZealotException(String msg) {
        super(msg);
    }

    /**
     * 附带日志消息参数的构造方法.
     * @param msg 日志消息
     * @param t Throwable对象
     */
    public ZealotException(String msg, Throwable t) {
        super(msg, t);
    }

}
