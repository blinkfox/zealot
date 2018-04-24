package com.blinkfox.zealot.config;

import com.blinkfox.zealot.helpers.CollectionHelper;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.log.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * zealot的XML文件包的扫描配置类.
 *
 * @author blinkfox on 2018/4/24.
 */
public final class XmlScanConfigure {

    private static final Log log = Log.get(XmlScanConfigure.class);

    /** 逗号. */
    private static final String COMMA = ",";

    /**
     * 私有构造方法.
     */
    private XmlScanConfigure() {
        super();
    }

    /**
     * 获取XmlScanConfigure最新实例的唯一方法.
     * @return XmlScanConfigure实例
     */
    public static XmlScanConfigure newInstance() {
        return new XmlScanConfigure();
    }

    /**
     * 扫描配置的zealot xml包下所有的xml文件，并将这些xml文件的zealot节点读取存储到内存中.
     *
     * @param xmlLocations xml所在的位置
     */
    public void scan(String xmlLocations) {
        if (StringHelper.isBlank(xmlLocations)) {
            return;
        }

        // 对配置的xml路径按逗号分割的规则来解析，如果是XML文件则直接解析该xml文件，
        // 否则就代表是xml资源目录，并解析目录下所有的xml文件.
        String[] xmlLocationArr = xmlLocations.split(COMMA);
        for (String xmlLocation: xmlLocationArr) {
            if (StringHelper.isBlank(xmlLocation)) {
                continue;
            }

            String cleanXmlLocation = xmlLocation.trim();
            if (cleanXmlLocation.endsWith(".xml")) {
                this.scanXml(cleanXmlLocation);
            } else {
                this.scanXmlsByPackage(cleanXmlLocation.replace('.', '/'));
            }
        }
    }

    /**
     * 根据指定的一个包扫描其下所有的xml文件.
     */
    private void scanXmlsByPackage(String xmlPackage) {
        List<URL> urls;
        try {
            urls = Collections.list(Thread.currentThread().getContextClassLoader().getResources(xmlPackage));
        } catch (IOException e) {
            log.error("无法解析配置的zealot xml路径下文件的URL，将被忽略.该路径为:" + xmlPackage + "，请检查!", e);
            return;
        }
        if (CollectionHelper.isEmpty(urls)) {
            return;
        }

        List<String> xmls = new ArrayList<String>();
        for (URL url: urls) {
            try {
                xmls.addAll(DefaultVfs.newInstance().list(url, xmlPackage));
            } catch (Exception e) {
                // 此处忽略异常堆栈信息.
                log.error("解析zealot xml包存在些问题,将被忽略！xml包为:" + xmlPackage + ",url:" + url);
            }
        }

        log.info("xmls:" + xmls.toString());
    }

    /**
     * 根据指定的xml文件.
     */
    private void scanXml(String xmlFile) {

    }



}