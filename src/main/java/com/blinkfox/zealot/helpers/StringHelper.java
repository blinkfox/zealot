package com.blinkfox.zealot.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * Created by blinkfox on 2016/10/30.
 */
public class StringHelper {
	
	/**
	 * 私有的构造方法
	 */
	private StringHelper() {
		
	}

    /**
     * 将字符串中的“空格（包括换行、回车、制表符）”等转成空格来处理，最后去掉所有多余空格
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        Pattern p = Pattern.compile("\\|\t|\r|\n");
        Matcher m = p.matcher(str);
        return m.replaceAll("").replaceAll("\\s{2,}", " ").trim();
    }

    /**
     * 判断是否为空字符串，包括空格也算
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if(str == null || (strLen = str.length()) == 0) {
            return true;
        }

        // 遍历每个空格是否有非空格元素
        for(int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
	
}