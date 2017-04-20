package com.blinkfox.zealot.helpers;

import com.blinkfox.zealot.log.Log;
import java.io.IOException;
import java.io.Reader;

/**
 * 输入输出相关的工具类.
 * Created by blinkfox on 2017/4/21.
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
     * “安静的”关闭Reader.
     * @param input reader实例
     */
    public static void closeQuietly(Reader input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                log.error("关闭Reader实例出错！", e);
            }
        }
    }

}