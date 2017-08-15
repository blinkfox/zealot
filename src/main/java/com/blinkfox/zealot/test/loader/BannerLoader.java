package com.blinkfox.zealot.test.loader;

import com.blinkfox.zealot.test.log.Log;

/**
 * 加载 zealot banner 加载器类.
 * Created by blinkfox on 2017/4/21.
 */
public class BannerLoader {
    
    private static final Log log = Log.get(BannerLoader.class);

    /** zealot 的 banner 文本. */
    private static final String BANNER_TEXT = "\n"
            + "__________               .__           __\n"
            + "\\____    / ____  _____   |  |    _____/  |_\n"
            + "  /     /_/ __ \\ \\__  \\  |  |   /  _ \\   __\\\n"
            + " /     /_\\  ___/  / __ \\_|  |__(  <_> )  |\n"
            + "/_______ \\\\___  >(____  /|____/ \\____/|__|\n"
            + "        \\/    \\/      \\/\n";

    /**
     * 私有构造方法.
     */
    private BannerLoader() {
        super();
    }

    /**
     * 获取 BannerLoader 加载器的实例.
     * @return BannerLoader新的实例
     */
    public static BannerLoader newInstance() {
        return new BannerLoader();
    }

    /**
     * 是否打印Banner信息，如果参数为true,则打印否则不打印.
     * @param isPrint 是否打印
     */
    public void print(boolean isPrint) {
        if (isPrint) {
            log.warn(BANNER_TEXT);
        }
    }

}