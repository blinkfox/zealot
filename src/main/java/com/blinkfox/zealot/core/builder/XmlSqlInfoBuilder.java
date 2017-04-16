package com.blinkfox.zealot.core.builder;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
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
     * 构建普通类型查询的sqlInfo信息.
     * @param fieldText 字段文本值
     * @param valueText 参数值
     * @param suffix 后缀，如：大于、等于、小于等
     * @return 返回SqlInfo信息
     */
    public SqlInfo buildNormalSql(String fieldText, String valueText, String suffix) {
        Object value = ParseHelper.parseExpressWithException(valueText, context);
        return super.buildNormalSql(fieldText, value, suffix);
    }

    /**
     * 构建Like模糊查询的sqlInfo信息.
     * @param fieldText 字段文本值
     * @param valueText 参数值
     * @return 返回SqlInfo信息
     */
    public SqlInfo buildLikeSql(String fieldText, String valueText) {
        return super.buildLikeSql(fieldText, ParseHelper.parseExpressWithException(valueText, context));
    }

    /**
     * 构建between区间查询的sqlInfo信息.
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
     * 构建in范围查询的sqlInfo信息.
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
        Object[] values = this.convertToArray(obj);
        return super.buildInSql(fieldText, values);
    }

    /**
     * 构建任意文本和自定义有序参数集合来构建的sqlInfo信息.
     * @param valueText value参数值
     * @return 返回SqlInfo信息
     */
    @SuppressWarnings("rawtypes")
    public SqlInfo buildTextSqlParams(String valueText) {
        // 获取value值，判断是否为空，若为空，则直接退出本方法
        Object obj = ParseHelper.parseExpressWithException(valueText, context);
        obj = obj == null ? new Object(){} : obj;

        Object[] values = this.convertToArray(obj);
        for (Object objVal: values) {
            this.sqlInfo.getParams().add(objVal);
        }
        return sqlInfo;
    }

    /**
     * 将对象转成数组.
     * @param obj 对象
     * @return 数组
     */
    private Object[] convertToArray(Object obj) {
        // 获取参数的集合信息，并转换成数组
        Object[] values;
        if (obj instanceof Collection) {
            values = ((Collection) obj).toArray();
        } else if (obj.getClass().isArray()) {
            values = (Object[]) obj;
        } else {
            values = new Object[]{obj};
        }
        return values;
    }

}