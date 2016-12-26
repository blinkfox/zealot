package com.blinkfox.zealot.config;

import com.blinkfox.zealot.bean.XmlContext;

/**
 * 自定义测试的Zealot配置类
 * @author blinkfox
 * @date 2016-12-26
 */
public class MyZealotConfig extends AbstractZealotConfig {
	
	public static final String USER_ZEALOT = "user_zealot";

	/**
	 * zealot的xml SQL文件和命名空间配置
	 */
	@Override
	public void configXml(XmlContext ctx) {
		ctx.add(USER_ZEALOT, "com/blinkfox/zealot/xml/zealot-user.xml");
	}

	/**
	 * zealot的自定义标签及其处理器配置
	 */
	@Override
	public void configTagHandler() {
		
	}

}