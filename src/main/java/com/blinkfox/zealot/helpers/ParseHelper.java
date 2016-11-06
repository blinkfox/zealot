package com.blinkfox.zealot.helpers;

import com.blinkfox.zealot.log.Log;
import org.mvel2.MVEL;
import com.blinkfox.zealot.bean.BuildSource;

/**
 * OGNL表达式相关的工具类
 * Created by blinkfox on 2016/10/30.
 */
public class ParseHelper {

    // 得到 log 实例
    private static final Log log = Log.get(ParseHelper.class);
	
	/**
	 * 私有构造方法
	 */
	private ParseHelper() {
		super();
	}

	/**
     * 通过MVEL来解析表达式的值
     * @param exp 待解析表达式
     * @param paramObj 参数对象
     * @return 返回解析后的值
     */
    public static Object parseWithMvel(String exp, Object paramObj) {
        Object obj = false;
        try {
            obj = MVEL.eval(exp, paramObj);
        } catch (Exception e) {
            log.error("MVEL表达式执行出错:" + exp, e);
        }
        return obj;
    }
	
}