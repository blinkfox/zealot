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
     * @return
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
     * @return
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
     * @return
     */
    public static SqlInfo buildBetweenSql(BuildSource source, String fieldText,
                                          String startText, String endText) {
        init(source);

        /* 根据开始文本和结束文本判断执行是大于、小于还是区间的查询sql和参数的生成 */
        if (StringHelper.isNotBlank(startText) &&
                StringHelper.isBlank(endText)) { // 开始不为空，结束为空的情况
            join.append(source.getPrefix()).append(fieldText).append(ZealotConst.GT_SUFFIX);
            params.add(ParseHelper.parseWithMvel(startText, source.getParamObj()));
        } else if (StringHelper.isBlank(startText) &&
                StringHelper.isNotBlank(endText)) { // 开始为空，结束不为空的情况
            join.append(source.getPrefix()).append(fieldText).append(ZealotConst.LT_SUFFIX);
            params.add(ParseHelper.parseWithMvel(endText, source.getParamObj()));
        } else if (StringHelper.isNotBlank(startText) &&
                StringHelper.isNotBlank(endText)) { // 开始、结束均不为空的情况
            join.append(source.getPrefix()).append(fieldText).append(ZealotConst.BT_AND_SUFFIX);
            params.add(ParseHelper.parseWithMvel(startText, source.getParamObj()));
            params.add(ParseHelper.parseWithMvel(endText, source.getParamObj()));
        }

        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建Like模糊查询的sql信息
     * @param source 构建所需的资源对象
     * @param fieldText 字段文本值
     * @param valueText 参数值
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static SqlInfo buildInSql(BuildSource source, String fieldText, String valueText) {
        init(source);

        Object[] values = new Object[] {};
        join.append(source.getPrefix()).append(fieldText).append(ZealotConst.IN_SUFFIX).append("(");

        // 获取参数的集合信息，并转换成数组
        Object obj = ParseHelper.parseWithMvel(valueText, source.getParamObj());
        if (obj instanceof Collection) {
            values = ((Collection) obj).toArray();
        } else if (obj.getClass().isArray()) {
            values = (Object[]) obj;
        }

        // 遍历数组，并遍历添加in查询的替换符和参数
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