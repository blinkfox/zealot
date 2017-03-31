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
     */
    void init(BuildSource source) {
        this.sqlInfo = source.getSqlInfo();
        this.join = sqlInfo.getJoin();
        this.params = sqlInfo.getParams();
        this.context = source.getParamObj();
        this.prefix = source.getPrefix();
    }

    /**
     * 构建等值查询需要的StrinbBuider join片段信息.
     * @param fieldText 数据库字段的文本
     * @return sqlInfo
     */
    SqlInfo buildEqualJoin(String fieldText) {
        join.append(prefix).append(fieldText).append(ZealotConst.EQUAL_SUFFIX);
        return sqlInfo.setJoin(join);
    }
}