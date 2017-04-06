package com.blinkfox.zealot.core.builder;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.exception.NotCollectionOrArrayException;
import com.blinkfox.zealot.helpers.ParseHelper;
import java.util.Collection;

/**
 * 构建使用xml拼接sql片段的工具类.
 * Created by blinkfox on 2016/10/30.
 */
public final class XmlSqlInfoBuilder extends SqlInfoBuilder {
    
    /**
     * 私有构造方法.
     */
    private XmlSqlInfoBuilder() {
        super();
    }

    /**
     * 获取XmlSqlInfoBuilder的实例，并初始化属性信息.
     * @param source BuildSource实例
     * @return XmlSqlInfoBuilder实例     */
    public static XmlSqlInfoBuilder newInstace(BuildSource source) {
        XmlSqlInfoBuilder builder = new XmlSqlInfoBuilder();
        builder.init(source);
        return builder;
    }

    /**
     * 构建普通等值查询的sql信息.
     * @param fieldText 字段文本值
     * @param valueText 参数值
     * @return 返回SqlInfo信息
     */
    public SqlInfo buildEqualSql(String fieldText, String valueText) {
        return super.buildEqualSql(fieldText, ParseHelper.parseExpressWithException(valueText, context));
    }

    /**
     * 构建Like模糊查询的sql信息.
     * @param fieldText 字段文本值
     * @param valueText 参数值
     * @return 返回SqlInfo信息
     */
    public SqlInfo buildLikeSql(String fieldText, String valueText) {
        return super.buildLikeSql(fieldText, ParseHelper.parseExpressWithException(valueText, context));
    }

    /**
     * 构建数字查询的sql信息.
     * @param fieldText 字段文本值
     * @param startText 参数开始值
     * @param endText 参数结束值
     * @return 返回SqlInfo信息
     */
    public SqlInfo buildBetweenSql(String fieldText, String startText, String endText) {
        // 获取开始属性值和结束属性值,作区间查询
        Object startValue = ParseHelper.parseExpress(startText, context);
        Object endValue = ParseHelper.parseExpress(endText, context);
        return super.buildBetweenSql(fieldText, startValue, endValue);
    }

    /**
     * 构建Like模糊查询的sql信息.
     * @param fieldText 字段文本值
     * @param valueText 参数值
     * @return 返回SqlInfo信息
     */
    @SuppressWarnings("rawtypes")
    public SqlInfo buildInSql(String fieldText, String valueText) {
        // 获取value值，判断是否为空，若为空，则直接退出本方法
        Object obj = ParseHelper.parseExpressWithException(valueText, context);
        if (obj == null) {
            return sqlInfo;
        }

        // 获取参数的集合信息，并转换成数组
        Object[] values;
        if (obj instanceof Collection) {
            values = ((Collection) obj).toArray();
        } else if (obj.getClass().isArray()) {
            values = (Object[]) obj;
        } else {
            throw new NotCollectionOrArrayException("in查询xml标签中的值解析后不是有效的集合或数组!");
        }

        return super.buildInSql(fieldText, values);
    }

}