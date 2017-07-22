package com.blinkfox.zealot.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载zealot banner.txt文件的加载器类.
 * Created by blinkfox on 2017/4/21.
 */
public class BannerLoader {
    
    private static final Logger log = LoggerFactory.getLogger(BannerLoader.class);

    /** banner文本. */
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
     * 打印banner文件中的内容.
     */
    public void print() {
        log.info(BANNER_TEXT);
    }

}