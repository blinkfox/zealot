package com.blinkfox.zealot.test.handler;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.annotation.Tagger;
import com.blinkfox.zealot.config.annotation.Taggers;
import com.blinkfox.zealot.core.IConditHandler;

/**
 * 测试'Taggers'注解的Handler.
 * @see com.blinkfox.zealot.config.annotation.Taggers
 *
 * @author blinkfox on 2018/4/28.
 */
@Taggers({
        @Tagger(value = "hello", prefix = "hello", symbol = "blinkfox"),
        @Tagger(value = "hi", prefix = "hi", symbol = "blinkfox"),
        @Tagger(value = "hw", prefix = "hello", symbol = "world")
})
public class TaggersTestHandler implements IConditHandler {

    /**
     * 由于这个类只是用来测试注解，所以这里只做简单的字符串拼接.
     * @param source 构建所需的资源对象
     * @return sqlInfo
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        source.getSqlInfo().getJoin().append(source.getPrefix())
                .append(" ").append(source.getSuffix());
        return source.getSqlInfo();
    }

}