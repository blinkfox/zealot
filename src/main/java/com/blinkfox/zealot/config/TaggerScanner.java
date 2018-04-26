package com.blinkfox.zealot.config;

import com.blinkfox.zealot.config.scanner.Scanner;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.log.Log;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * zealot Handler上的xml标签注解'@Tagger'扫描类.
 * 将扫描的类添加到'AbstractZealotConfig.tagHandlerMap'中，供后续配置使用.
 *
 * @author blinkfox on 2018/4/26.
 */
public final class TaggerScanner implements Scanner {

    private static Log log = Log.get(TaggerScanner.class);

    /** 存放所有扫描位置下的class对象的Set集合. */
    private Set<Class<?>> classSet;

    /**
     * 私有构造方法.
     */
    private TaggerScanner() {
        this.classSet = new HashSet<Class<?>>();
    }

    /**
     * 获取 TaggerScanner 最新实例的唯一方法.
     * @return TaggerScanner实例
     */
    public static TaggerScanner newInstance() {
        return new TaggerScanner();
    }

    /**
     * 扫描配置的zealot handler包下所有的class.
     * 并将含有'@Tagger'和'@Taggers'的注解的Class解析出来，存储到tagHandlerMap配置中.
     *
     * @param handlerLocations handler所在的位置
     */
    public void scan(String handlerLocations) {
        if (StringHelper.isBlank(handlerLocations)) {
            return;
        }

        // 对配置的xml路径按逗号分割的规则来解析，如果是XML文件则直接将该xml文件存放到xmlPaths的Set集合中，
        // 否则就代表是xml资源目录，并解析目录下所有的xml文件，将这些xml文件存放到xmlPaths的Set集合中，
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String[] locationArr = handlerLocations.split(ZealotConst.COMMA);
        for (String location: locationArr) {
            if (StringHelper.isBlank(location)) {
                continue;
            }

            // 判断文件如果是具体的Java文件和class文件，则将文件解析成Class对象.
            // 如果都不是，则视其为包,然后解析该包及子包下面的所有class文件.
            String cleanLocation = location.trim();
            if (StringHelper.isJavaFile(cleanLocation) || StringHelper.isClassFile(cleanLocation)) {
                this.addClassByName(classLoader, cleanLocation.substring(0, cleanLocation.lastIndexOf('.')));
            } else {
                this.addClassByPackage(cleanLocation.replace('.', '/'), classLoader);
            }
        }
    }

    /**
     * 根据classLoader和className找到对应的class对象.
     * @param classLoader ClassLoader实例
     * @param className class全路径名
     */
    private void addClassByName(ClassLoader classLoader, String className) {
        try {
            classSet.add(classLoader.loadClass(className));
        } catch (ClassNotFoundException expected) {
            // 由于是扫描package下的class，即时出现异常，也忽略掉.
            log.warn("【警告】未找到class类:'" + className + "'，将忽略不解析此类.");
        }
    }

    /**
     * 根据包名和Classloader实例，将该包下的所有Class存放到classSet集合中.
     *
     * @param packageName 包名
     * @param classLoader ClassLoader实例
     */
    private void addClassByPackage(String packageName, ClassLoader classLoader) {
        // 根据包名和Classloader实例，得到该包的URL Enumeration.
        Enumeration<URL> urlEnum = this.getUrlsByPackge(packageName, classLoader);
        if (urlEnum == null) {
            return;
        }

        while (urlEnum.hasMoreElements()) {
            URL url = urlEnum.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                log.info("扫描文件.");
                //String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
            } else if ("jar".equals(protocol)) {
                log.info("扫描jar包.");
            }
        }
    }

    /**
     * 根据包名和Classloader实例，得到该包的URL Enumeration.
     * @param packageName 包全路径名
     * @param classLoader  ClassLoader实例
     * @return URL枚举
     */
    private Enumeration<URL> getUrlsByPackge(String packageName, ClassLoader classLoader) {
        try {
            return classLoader.getResources(packageName);
        } catch (IOException e) {
            // 由于是扫描package下的class，即时出现异常，也忽略掉.
            log.warn("【警告】未找到包:'" + packageName + "'下的URL，将忽略此种错误情况.");
            return null;
        }
    }

}