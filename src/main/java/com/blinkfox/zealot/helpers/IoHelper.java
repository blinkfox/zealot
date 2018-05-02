package com.blinkfox.zealot.helpers;

import com.blinkfox.zealot.log.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * 输入输出相关的工具类.
 * @author blinkfox on 2017/4/21.
 */
public final class IoHelper {

    private static final Log log = Log.get(IoHelper.class);

    /**
     * 私有构造方法.
     */
    private IoHelper() {
        super();
    }

    /**
     * “安静的”关闭资源.
     * @param closeable reader实例
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // 忽略，不打印异常堆栈信息.
                log.error("关闭Reader实例出错！");
            }
        }
    }

}