package com.blinkfox.zealot.test.config;

import com.blinkfox.zealot.config.AbstractZealotConfig;
import com.blinkfox.zealot.config.entity.XmlContext;
import com.blinkfox.zealot.test.handler.UserIdEmailHandler;

/**
 * 自定义测试的Zealot配置类.
 * @author blinkfox on 2016/12/26.
 */
public class MyZealotConfig extends AbstractZealotConfig {

    /** xml命名空间标识. */
    public static final String USER_ZEALOT = "userZealot";
    public static final String STUDENT_ZEALOT = "studentZealot";

    /**
     * zealot的xml SQL文件和命名空间配置.
     * @param ctx xmlContext对象
     */
    @Override
    public void configXml(XmlContext ctx) {
        ctx.add(USER_ZEALOT, "zealot-user.xml");
        ctx.add(STUDENT_ZEALOT, "zealot-student.xml");
    }

    /**
     * zealot的自定义标签及其处理器配置.
     */
    @Override
    public void configTagHandler() {
        // 自定义userIdEmail标签和处理器
        add("userIdEmail", UserIdEmailHandler.class);
        // 有and前缀的自定义标签
        add("andUserIdEmail", " and " ,UserIdEmailHandler.class);
    }

}