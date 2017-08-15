package com.blinkfox.zealot.test.config.entity;

/**
 * 普通配置信息的实体类.
 * Created by blinkfox on 2017/07/24.
 */
public class NormalConfig {

    /** 是否开发调试模式. */
    private boolean isDebug;

    /** 是否打印Banner信息. */
    private boolean isPrintBanner;

    /** 是否打印SqlInfo信息. */
    private boolean isPrintSqlInfo;

    /** NormalConfig唯一实例. */
    private static final NormalConfig normalConfig = new NormalConfig();

    /**
     * 私有构造方法.
     */
    private NormalConfig() {
        this.setDebug(false).setPrintBanner(true).setPrintSqlInfo(true);
    }

    /**
     * 得到新的具有默认属性的`NormalConfig`实例.
     * <p>默认配置为:debug模式为false,加载配置信息完毕后打印Banner，打印SQL信息.</p>
     * @return NormalConfig实例.
     */
    public static NormalConfig getInstance() {
        return normalConfig;
    }

    /**
     * 获取是否开启了debug模式的布尔值.
     * @return 布尔值
     */
    public boolean isDebug() {
        return this.isDebug;
    }

    /**
     * 设置是否开启debug模式.
     * @param debug 是否开启debug模式
     * @return NormalConfig实例
     */
    public NormalConfig setDebug(boolean debug) {
        this.isDebug = debug;
        return this;
    }

    /**
     * 获取加载完毕之后是否打印Banner.
     * @return 布尔值
     */
    public boolean isPrintBanner() {
        return this.isPrintBanner;
    }

    /**
     * 设置加载完毕之后是否打印Banner.
     * @param printBanner 是否打印Banner的布尔值
     * @return NormalConfig实例
     */
    public NormalConfig setPrintBanner(boolean printBanner) {
        this.isPrintBanner = printBanner;
        return this;
    }

    /**
     * 获取是否打印SqlInfo的日志信息.
     * @return 布尔值
     */
    public boolean isPrintSqlInfo() {
        return this.isPrintSqlInfo;
    }

    /**
     * 设置是否打印SQLInfo的日志信息.
     * @param printSqlInfo 是否打印SqlInfo的布尔值
     * @return NormalConfig实例
     */
    public NormalConfig setPrintSqlInfo(boolean printSqlInfo) {
        this.isPrintSqlInfo = printSqlInfo;
        return this;
    }

}