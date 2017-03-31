package com.blinkfox.zealot.core.builder;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;

import java.util.List;

/**
 * 构建拼接sql片段工具类的父类.
 * Created by blinkfox on 2017-03-31.
 */
class SqlInfoBuilder {

    // sqlInfo对象
    SqlInfo sqlInfo;

    // sql拼接器，sqlInfo对象的属性
    StringBuilder join;

    // 有序的参数集合，sqlInfo对象的属性
    List<Object> params;

    // 上下文参数
    Object context;

    // 前缀
    String prefix;

    /**
     * 私有构造方法.
     */
    SqlInfoBuilder() {
        super();
    }

    /**
     * 根据构建的资源参数初始化数据.
     * @param source 构建sql的相关参数
     */
    void init(BuildSource source) {
        this.sqlInfo = source.getSqlInfo();
        this.join = sqlInfo.getJoin();
        this.params = sqlInfo.getParams();
        this.context = source.getParamObj();
        this.prefix = source.getPrefix();
    }

    /**
     * 构建等值查询需要的StrinbBuider join信息.
     * @param fieldText 数据库字段的文本
     * @param value 参数值
     * @return sqlInfo
     */
    SqlInfo doBuildEqualSql(String fieldText, Object value) {
        join.append(prefix).append(fieldText).append(ZealotConst.EQUAL_SUFFIX);
        params.add(value);
        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建like模糊查询需要的StrinbBuider join信息.
     * @param fieldText 数据库字段的文本
     * @param value 参数值
     * @return sqlInfo
     */
    SqlInfo doBuildLikeSql(String fieldText, Object value) {
        join.append(prefix).append(fieldText).append(ZealotConst.LIEK_SUFFIX);
        params.add("%" + value + "%");
        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建区间查询的sql信息.
     * @param fieldText 数据库字段文本
     * @param startValue 参数开始值
     * @param endValue 参数结束值
     * @return 返回SqlInfo信息
     */
    SqlInfo doBuildBetweenSql(String fieldText, Object startValue, Object endValue) {
        /* 根据开始文本和结束文本判断执行是大于、小于还是区间的查询sql和参数的生成 */
        if (startValue != null && endValue == null) { // 开始不为空，结束为空的情况
            join.append(prefix).append(fieldText).append(ZealotConst.GT_SUFFIX);
            params.add(startValue);
        } else if (startValue == null && endValue != null) { // 开始为空，结束不为空的情况
            join.append(prefix).append(fieldText).append(ZealotConst.LT_SUFFIX);
            params.add(endValue);
        } else { // 开始、结束均不为空的情况
            join.append(prefix).append(fieldText).append(ZealotConst.BT_AND_SUFFIX);
            params.add(startValue);
            params.add(endValue);
        }

        return sqlInfo.setJoin(join).setParams(params);
    }

}