package com.blinkfox.zealot.loader;

import com.blinkfox.zealot.helpers.IoHelper;
import com.blinkfox.zealot.log.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;

/**
 * 加载zealot banner.txt文件的加载器类.
 * Created by blinkfox on 2017/4/21.
 */
public class BannerLoader {

    private static final Log log = Log.get(BannerLoader.class);

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
        URL banner = this.getClass().getResource("/banner.txt");
        if (banner != null) {
            log.info(this.txtToStr(new File(banner.getPath())));
        }
    }

    /**
     * 获取txt文件中的内容并转换为字符串.
     * @param file 文件
     * @return 字符串
     */
    private String txtToStr(File file) {
        StringBuilder sb = new StringBuilder("");
        Reader fileReader = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(fileReader);

            // 一行一行的读取字符串，并将结果追加到StringBuilder中
            String line;
            while ((line = bufferReader.readLine()) != null) {
                sb.append(System.getProperty("line.separator")).append(line);
            }
        } catch (Exception e) {
            log.error("打印zealot banner失败！", e);
        } finally {
            IoHelper.closeQuietly(fileReader);
        }

        return sb.toString();
    }

}