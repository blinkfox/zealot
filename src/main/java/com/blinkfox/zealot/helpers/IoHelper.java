package com.blinkfox.zealot.helpers;

import com.blinkfox.zealot.log.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * 输入输出相关的工具类.
 * Created by blinkfox on 2017/4/21.
 */
public final class IoHelper {

    private static final Log log = Log.get(IoHelper.class);

    /** 逗号. */
    private static final String XML_EXT = ".xml";

    /**
     * 私有构造方法.
     */
    private IoHelper() {
        super();
    }

    /**
     * 根据给定的文件路径判断文件是否是XML文件.
     * @param filePath 文件路径
     * @return 布尔值
     */
    public static boolean isXmlFile(String filePath) {
        return filePath != null && filePath.endsWith(XML_EXT);
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