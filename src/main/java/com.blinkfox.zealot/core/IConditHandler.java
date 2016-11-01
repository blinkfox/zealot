package com.blinkfox.zealot.core;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;

/**
 * 构建动态条件sql和参数的抽象接口
 * Created by blinkfox on 2016/10/30.
 */
public interface IConditHandler {

    /**
     * 构建sqlInfo信息
     * @param source
     * @return
     */
    SqlInfo buildSqlInfo(BuildSource source);

}