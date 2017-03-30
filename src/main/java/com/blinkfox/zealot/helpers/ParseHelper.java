package com.blinkfox.zealot.helpers;

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
     * 通过MVEL来解析表达式的值.
     * @param exp 待解析表达式
     * @param paramObj 参数对象
     * @return 返回解析后的值
     */
    public static Object parseWithMvel(String exp, Object paramObj) {
        Object obj = null;
        try {
            obj = MVEL.eval(exp, paramObj);
        } catch (Exception e) {
            log.error("MVEL表达式执行出错,表达式是:" + exp, e);
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
        String output = null;
        try {
            output = (String) TemplateRuntime.eval(template, paramObj);
        } catch (Exception e) {
            log.error("MVEL模版执行出错,模板是:" + template, e);
        }
        return output;
    }

}