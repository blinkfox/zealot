package com.blinkfox.zealot.exception;

/**
 * zealot配置未找到的异常.
 * Created by blinkfox on 2016/11/7.
 */
public class ConfigNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息组成的构造方法.
     * @param msg 日志消息
     */
    public ConfigNotFoundException(String msg) {
        super(msg);
    }

    /**
     * 附带日志消息和异常信息组成的构造方法.
     * @param msg 日志消息
     * @param t Throwable对象
     */
    public ConfigNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

}