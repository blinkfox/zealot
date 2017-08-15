package com.blinkfox.zealot.core.concrete;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.core.IConditHandler;

/**
 * 引用标签对应的动态sql生成处理器的实现类.
 * <p>引用标签的主要内容：`<include match="" filePath="" zealotid="" />`</p>
 * Created by blinkfox on 2017/8/15.
 */
public class IncludeHandler implements IConditHandler {
    /**
     * 构建sqlInfo信息.
     * @param source 构建所需的资源对象
     * @return 返回SqlInfo对象
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        return null;
    }

}