package com.blinkfox.zealot.helpers;

import com.blinkfox.zealot.exception.ParseExpressionException;
import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;

/**
 * MVEL表达式相关的工具类.
 * Created by blinkfox on 2016/10/30.
 */
public final class ParseHelper {

    /**
     * 私有构造方法.
     */
    private ParseHelper() {
        super();
    }

    /**
     * 通过MVEL来解析表达式的值.
     * @param exp 待解析表达式
     * @param paramObj 参数对象
     * @return 返回解析后的值
     */
    public static Object parseWithMvel(String exp, Object paramObj) {
        Object obj;
        try {
            obj = MVEL.eval(exp, paramObj);
        } catch (Exception e) {
            throw new ParseExpressionException("解析Mvel表达式异常，解析出错的表达式为:" + exp, e);
        }
        return obj;
    }

    /**
     * 通过MVEL来解析模板的值.
     * @param template 待解析表达式
     * @param paramObj 参数对象
     * @return 返回解析后的结果
     */
    public static String parseTemplate(String template, Object paramObj) {
        String output;
        try {
            output = (String) TemplateRuntime.eval(template, paramObj);
        } catch (Exception e) {
            throw new ParseExpressionException("解析Mvel模板异常，解析出错的模板为:" + template, e);
        }
        return output;
    }

}