package com.blinkfox.zealot.helpers;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 构建sql查询相关的帮助类
 * Created by blinkfox on 2016/10/30.
 */
public class BuildSqlInfoHelper {

    private static SqlInfo sqlInfo = null; // sqlInfo对象
    private static StringBuffer join = null; // sql拼接器
    private static ArrayList<Object> params = null; // 有序的参数结合

    /**
     * 根据构建的资源参数初始化数据
     * @param source
     */
    private static void init(BuildSource source) {
        sqlInfo = source.getSqlInfo();
        join = sqlInfo.getJoin();
        params = sqlInfo.getParams();
    }

    /**
     * 构建普通的sql信息
     * @param source
     * @param fieldText
     * @param valueText
     * @return
     */
    public static SqlInfo buildEqualSql(BuildSource source, String fieldText, String valueText) {
        init(source);

        join.append(source.getPrefix()).append(fieldText).append(ZealotConst.EQUAL_SUFFIX);
        params.add(ParseHelper.parseWithMvel(valueText, source));

        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建Like模糊查询的sql信息
     * @param source
     * @param fieldText
     * @param valueText
     * @return
     */
    public static SqlInfo buildLikeSql(BuildSource source, String fieldText, String valueText) {
        init(source);

        join.append(source.getPrefix()).append(fieldText).append(ZealotConst.LIEK_SUFFIX);
        Object obj = ParseHelper.parseWithMvel(valueText, source);
        params.add("%" + obj + "%");

        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建数字查询的sql信息
     * @param source
     * @param fieldText
     * @param startText
     * @param endText
     * @return
     */
    public static SqlInfo buildBetweenSql(BuildSource source, String fieldText,
                                          String startText, String endText) {
        init(source);

        /* 根据开始文本和结束文本判断执行是大于、小于还是区间的查询sql和参数的生成 */
        if (StringHelper.isNotBlank(startText) &&
                StringHelper.isBlank(endText)) { // 开始不为空，结束为空的情况
            join.append(source.getPrefix()).append(fieldText).append(ZealotConst.GT_SUFFIX);
            params.add(ParseHelper.parseWithMvel(startText, source));
        } else if (StringHelper.isBlank(startText) &&
                StringHelper.isNotBlank(endText)) { // 开始为空，结束不为空的情况
            join.append(source.getPrefix()).append(fieldText).append(ZealotConst.LT_SUFFIX);
            params.add(ParseHelper.parseWithMvel(endText, source));
        } else if (StringHelper.isNotBlank(startText) &&
                StringHelper.isNotBlank(endText)) { // 开始、结束均不为空的情况
            join.append(source.getPrefix()).append(fieldText).append(ZealotConst.BT_AND_SUFFIX);
            params.add(ParseHelper.parseWithMvel(startText, source));
            params.add(ParseHelper.parseWithMvel(endText, source));
        }

        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建Like模糊查询的sql信息
     * @param source
     * @param fieldText
     * @param valueText
     * @return
     */
    public static SqlInfo buildInSql(BuildSource source, String fieldText, String valueText) {
        init(source);

        Object[] values = new Object[] {};
        join.append(source.getPrefix()).append(fieldText).append(ZealotConst.IN_SUFFIX).append("(");

        // 获取参数的集合信息，并转换成数组
        Object obj = ParseHelper.parseWithMvel(valueText, source);
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