package com.blinkfox.zealot.helpers;

import java.util.Collection;
import java.util.List;
import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;

/**
 * 构建sql查询相关的帮助类
 * Created by blinkfox on 2016/10/30.
 */
public class BuildSqlInfoHelper {
	
	private static SqlInfo sqlInfo = null; // sqlInfo对象
    private static StringBuilder join = null; // sql拼接器
    private static List<Object> params = null; // 有序的参数结合
    
    /**
     * 私有构造方法
     */
    private BuildSqlInfoHelper() {
    	super();
    }

    /**
     * 根据构建的资源参数初始化数据
     * @param source 构建所需的资源对象
     */
    private static void init(BuildSource source) {
        sqlInfo = source.getSqlInfo();
        join = sqlInfo.getJoin();
        params = sqlInfo.getParams();
    }

    /**
     * 构建普通的sql信息
     * @param source 构建所需的资源对象
     * @param fieldText 字段文本值
     * @param valueText 参数值
     * @return 返回SqlInfo信息
     */
    public static SqlInfo buildEqualSql(BuildSource source, String fieldText, String valueText) {
        init(source);

        join.append(source.getPrefix()).append(fieldText).append(ZealotConst.EQUAL_SUFFIX);
        params.add(ParseHelper.parseWithMvel(valueText, source.getParamObj()));

        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建Like模糊查询的sql信息
     * @param source 构建所需的资源对象
     * @param fieldText 字段文本值
     * @param valueText 参数值
     * @return 返回SqlInfo信息
     */
    public static SqlInfo buildLikeSql(BuildSource source, String fieldText, String valueText) {
        init(source);

        join.append(source.getPrefix()).append(fieldText).append(ZealotConst.LIEK_SUFFIX);
        Object obj = ParseHelper.parseWithMvel(valueText, source.getParamObj());
        params.add("%" + obj + "%");

        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建数字查询的sql信息
     * @param source 构建所需的资源对象
     * @param fieldText 字段文本值
     * @param startText 参数开始值
     * @param endText 参数结束值
     * @return 返回SqlInfo信息
     */
    public static SqlInfo buildBetweenSql(BuildSource source, String fieldText,
            String startText, String endText) {
        init(source);

        // 获取开始属性值和结束属性值
        Object startValue = ParseHelper.parseWithMvel(startText, source.getParamObj());
        Object endValue = ParseHelper.parseWithMvel(endText, source.getParamObj());

        /* 根据开始文本和结束文本判断执行是大于、小于还是区间的查询sql和参数的生成 */
        if (startValue != null && endValue == null) { // 开始不为空，结束为空的情况
            join.append(source.getPrefix()).append(fieldText).append(ZealotConst.GT_SUFFIX);
            params.add(startValue);
        } else if (startValue == null && endValue != null) { // 开始为空，结束不为空的情况
            join.append(source.getPrefix()).append(fieldText).append(ZealotConst.LT_SUFFIX);
            params.add(endValue);
        } else { // 开始、结束均不为空的情况
            join.append(source.getPrefix()).append(fieldText).append(ZealotConst.BT_AND_SUFFIX);
            params.add(startValue);
            params.add(endValue);
        }

        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建Like模糊查询的sql信息
     * @param source 构建所需的资源对象
     * @param fieldText 字段文本值
     * @param valueText 参数值
     * @return 返回SqlInfo信息
     */
    @SuppressWarnings("rawtypes")
	public static SqlInfo buildInSql(BuildSource source, String fieldText, String valueText) {
        init(source);

        // 获取value值，判断是否为空，若为空，则直接退出本方法
        Object obj = ParseHelper.parseWithMvel(valueText, source.getParamObj());
        if (obj == null) {
            return sqlInfo;
        }

        // 获取参数的集合信息，并转换成数组
        Object[] values = new Object[] {};
        if (obj instanceof Collection) {
            values = ((Collection) obj).toArray();
        } else if (obj.getClass().isArray()) {
            values = (Object[]) obj;
        }

        if (values.length == 0) {
            return sqlInfo;
        }

        // 遍历数组，并遍历添加in查询的替换符和参数
        join.append(source.getPrefix()).append(fieldText).append(ZealotConst.IN_SUFFIX).append("(");
        int len = values.length;
        for (int i = 0; i < len; i++) {
            if (i == (len - 1)) {
                join.append("?)");
            } else {
                join.append("?, ");
            }
            params.add(values[i]);
        }

        return sqlInfo.setJoin(join).setParams(params);
    }
	
}