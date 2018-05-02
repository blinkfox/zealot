package com.blinkfox.zealot.exception;

/**
 * Zealot xml 文件配置异常.
 * @author blinkfox on 2016/11/7.
 */
public class NodeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息参数的构造方法.
     * @param msg 日志消息
     */
    public NodeNotFoundException(String msg) {
        super(msg);
    }

}