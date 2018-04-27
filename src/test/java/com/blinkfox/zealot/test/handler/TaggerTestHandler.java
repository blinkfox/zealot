package com.blinkfox.zealot.test.handler;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.annotation.Tagger;
import com.blinkfox.zealot.core.IConditHandler;

/**
 * 测试Tagger注解的Handler.
 *
 * @author blinkfox on 2018/4/28.
 */
@Tagger(value = "helloTagger", prefix = "Hello", symbol = "World")
public class TaggerTestHandler implements IConditHandler {

    /**
     * 由于只是用来测试注解，所以这里不做任何实现.
     * @param source 构建所需的资源对象
     * @return sqlInfo
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        return source.getSqlInfo();
    }

}