package com.blinkfox.zealot.log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 基于 JDK Logging API 的日志打印工具类.
 * @author blinkfox on 2016/11/5.
 */
public class Log {

    /** jdk 自带的 logger. */
    private Logger logger;

    /**
     * 私有构造方法.
     * @param logger jdk Logger对象
     */
    private Log(Logger logger) {
        this.logger = logger;
    }

    /**
     * 构造并获取日志对象的实例.
     * @param cls 记录日志的类Class
     * @param <T> 泛型
     * @return 返回自身的实例
     */
    public static <T> Log get(Class<T> cls) {
        return new Log(Logger.getLogger(cls.getName()));
    }

    /**
     * 记录 info 级别的日志信息.
     * @param msg 日志消息
     */
    public void info(String msg) {
        logger.info(msg);
    }

    /**
     * 记录 warning 级别的日志信息.
     * @param msg 日志消息
     */
    public void warn(String msg) {
        logger.warning(msg);
    }

    /**
     * 记录 error 级别的日志信息.
     * @param msg 日志消息
     */
    public void error(String msg) {
        logger.severe(msg);
    }

    /**
     * 记录 error 级别的日志信息和异常信息.
     * @param msg 日志消息
     * @param t Throwable对象
     */
    public void error(String msg, Throwable t) {
        logger.log(Level.SEVERE, msg, t);
    }

}