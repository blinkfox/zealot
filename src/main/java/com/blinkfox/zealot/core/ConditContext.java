package com.blinkfox.zealot.core;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.AbstractZealotConfig;
import com.blinkfox.zealot.config.entity.TagHandler;
import com.blinkfox.zealot.exception.NodeNotFoundException;
import com.blinkfox.zealot.log.Log;

import java.util.Map;

/**
 * 构建动态条件查询的协调类.
 * Created by blinkfox on 2016/10/30.
 */
public final class ConditContext {

    private static final Log log = Log.get(ConditContext.class);

    /**
     * 私有构造方法.
     */
    private ConditContext() {
        super();
    }

    /**
     * 根据标签名称和对应的构建参数构造出对应标签的sql和参数.
     * @param source 构建所需的资源对象
     * @param tag 标签名称
     * @return 返回SqlInfo对象
     */
    public static SqlInfo buildSqlInfo(BuildSource source, String tag) {
        // 获取所有配置的标签和标签处理器的全局map对象，并得到对应标签的标签处理器
        // 如果符合就执行该标签中对应handler对象的方法
        Map<String, TagHandler> tagHandlerMap = AbstractZealotConfig.getTagHandlerMap();
        if (tagHandlerMap.containsKey(tag)) {
            TagHandler th = tagHandlerMap.get(tag);
            source.setPrefix(th.getPrefix()).setSuffix(th.getSuffix());
            return doBuildSqlInfo(source, th);
        } else {
            throw new NodeNotFoundException("未找到标签对应的处理器，该标签为:<" + tag + ">");
        }
    }

    /**
     * 执行构建SQL片段和参数的方法.
     * @param source 构建所需的资源对象
     * @param th 标签处理器实体
     * @return 返回SqlInfo对象
     */
    private static SqlInfo doBuildSqlInfo(BuildSource source, TagHandler th) {
        try {
            // 使用反射获取该Handler对应的实例，并执行方法
            IConditHandler handler = th.getHandlerCls().newInstance();
            return handler.buildSqlInfo(source);
        } catch (InstantiationException e) {
            log.error("实例化IConditHandler的实现类出错!", e);
        } catch (IllegalAccessException e) {
            log.error("访问Handler的实现类出错!", e);
        }
        return source.getSqlInfo();
    }

}