package com.blinkfox.zealot.exception;

/**
 * zealot配置未找到的异常
 * Created by blinkfox on 2016/11/7.
 */
public class ConfigNotFoundException extends RuntimeException {

    /**
     * 默认构造方法
     */
    public ConfigNotFoundException() {

    }

    /**
     * 使用日志消息组成的构造方法
     * @param msg 日志消息
     */
    public ConfigNotFoundException(String msg) {
        super(msg);
    }

    /**
     * 使用日志消息和异常信息组成的构造方法
     * @param message
     * @param cause
     */
    public ConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}