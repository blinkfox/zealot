package com.blinkfox.zealot.helpers;

import com.blinkfox.zealot.exception.ParseExpressionException;
import com.blinkfox.zealot.log.Log;

import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;

/**
 * MVEL表达式相关的工具类.
 * Created by blinkfox on 2016/10/30.
 */
public final class ParseHelper {

    private static final Log log = Log.get(ParseHelper.class);

    /**
     * 私有构造方法.
     */
    private ParseHelper() {
        super();
    }

    /**
     * 通过MVEL来解析表达式的值，如果出错不抛出异常.
     * @param exp 待解析表达式
     * @param paramObj 参数对象
     * @return 返回解析后的值
     */
    public static Object parseExpress(String exp, Object paramObj) {
        try {
            return MVEL.eval(exp, paramObj);
        } catch (Exception e) {
            log.error("解析表达式出错,表达式为:" + exp, e);
            return null;
        }
    }

    /**
     * 通过MVEL来解析表达式的值，如果出错则抛出异常.
     * @param exp 待解析表达式
     * @param paramObj 参数对象
     * @return 返回解析后的值
     */
    public static Object parseExpressWithException(String exp, Object paramObj) {
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