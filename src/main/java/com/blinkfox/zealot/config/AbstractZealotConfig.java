package com.blinkfox.zealot.config;

import static com.blinkfox.zealot.consts.ZealotConst.*;

import com.blinkfox.zealot.config.entity.NormalConfig;
import com.blinkfox.zealot.config.entity.TagHandler;
import com.blinkfox.zealot.config.entity.XmlContext;
import com.blinkfox.zealot.core.IConditHandler;
import com.blinkfox.zealot.core.concrete.BetweenHandler;
import com.blinkfox.zealot.core.concrete.InHandler;
import com.blinkfox.zealot.core.concrete.LikeHandler;
import com.blinkfox.zealot.core.concrete.NormalHandler;
import com.blinkfox.zealot.core.concrete.TextHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Node;

/**
 * Zealot主配置类.
 * Created by blinkfox on 2016/10/30.
 */
public abstract class AbstractZealotConfig {

    /**
     * 所有zealots XML文档的缓存map.
     * <p>key是资源的路径（将xml命名空间和zealotId用"@"符号分割），value是dom4j文档节点Node.</p>
     */
    private static Map<String, Node> zealots = new ConcurrentHashMap<String, Node>();

    /** 初始化默认的一些标签和tagHandlers到HashMap集合中,key是标签字符串,value是TagHandler对象. */
    private static Map<String, TagHandler> tagHandlerMap = new HashMap<String, TagHandler>();
    
    /* 添加默认的标签和对应的handler处理器，主要是普通条件,like,between,in等 */
    static {
        // 等于相关标签：equal、andEqual、orEqual
        add(EQUAL, NormalHandler.class, EQUAL_SUFFIX);
        add(AND_EQUAL, AND_PREFIX, NormalHandler.class, EQUAL_SUFFIX);
        add(OR_EQUAL, OR_PREFIX, NormalHandler.class, EQUAL_SUFFIX);

        // 不等于相关标签：notEqual、andNotEqual、orNotEqual
        add(NOT_EQUAL, NormalHandler.class, NOT_EQUAL_SUFFIX);
        add(AND_NOT_EQUAL, AND_PREFIX, NormalHandler.class, NOT_EQUAL_SUFFIX);
        add(OR_NOT_EQUAL, OR_PREFIX, NormalHandler.class, NOT_EQUAL_SUFFIX);

        // 大于相关标签：greaterThan、andGreaterThan、orGreaterThan
        add(MORE, NormalHandler.class, GT_SUFFIX);
        add(AND_MORE, AND_PREFIX, NormalHandler.class, GT_SUFFIX);
        add(OR_MORE, OR_PREFIX, NormalHandler.class, GT_SUFFIX);

        // 小于相关标签：lessThan、andGreater、orGreater
        add(LESS, NormalHandler.class, LT_SUFFIX);
        add(AND_LESS, AND_PREFIX, NormalHandler.class, LT_SUFFIX);
        add(OR_LESS, OR_PREFIX, NormalHandler.class, LT_SUFFIX);

        // 大于等于相关标签：greaterEqual、andGreaterEqual、orGreaterEqual
        add(MORE_EQUAL, NormalHandler.class, GTE_SUFFIX);
        add(AND_MORE_EQUAL, AND_PREFIX, NormalHandler.class, GTE_SUFFIX);
        add(OR_MORE_EQUAL, OR_PREFIX, NormalHandler.class, GTE_SUFFIX);

        // 小于等于相关标签：lessEqual、andLessEqual、orLessEqual
        add(LESS_EQUAL, NormalHandler.class, LTE_SUFFIX);
        add(AND_LESS_EQUAL, AND_PREFIX, NormalHandler.class, LTE_SUFFIX);
        add(OR_LESS_EQUAL, OR_PREFIX, NormalHandler.class, LTE_SUFFIX);

        // like相关标签：like、andLike、orLike
        add(LIKE, LikeHandler.class);
        add(AND_LIKE, AND_PREFIX, LikeHandler.class);
        add(OR_LIKE, OR_PREFIX, LikeHandler.class);

        // between相关标签：between、andBetween、orBetween
        add(BETWEEN, BetweenHandler.class);
        add(AND_BETWEEN, AND_PREFIX, BetweenHandler.class);
        add(OR_BETWEEN, OR_PREFIX, BetweenHandler.class);

        // in相关标签：in、andIn、orIn
        add(IN, InHandler.class);
        add(AND_IN, AND_PREFIX, InHandler.class);
        add(OR_IN, OR_PREFIX, InHandler.class);

        add(TEXT, TextHandler.class);
    }

    /**
     * 获取全局的Zealots文档缓存数据.
     * @return 返回xml命名空间和dom4j文件的缓存map
     */
    public static Map<String, Node> getZealots() {
        return zealots;
    }

    /**
     * 获取全局的标签和对应处理器的tagHandlerMap对象.
     * @return tagHandlerMap 标签和对应处理器的Map
     */
    public static Map<String, TagHandler> getTagHandlerMap() {
        return tagHandlerMap;
    }

    /**
     * 添加自定义标签和其对应的Handler class.
     * @param tagName 标签名称
     * @param handlerCls 动态处理类的反射类型
     */
    protected static void add(String tagName, Class<? extends IConditHandler> handlerCls) {
        tagHandlerMap.put(tagName, new TagHandler(handlerCls));
    }

    /**
     * 添加自定义标签和其对应的Handler class.
     * @param tagName 标签名称
     * @param prefix 前缀
     * @param handlerCls 动态处理类的反射类型
     */
    protected static void add(String tagName, String prefix, Class<? extends IConditHandler> handlerCls) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerCls));
    }

    /**
     * 添加自定义标签和其对应的Handler class.
     * @param tagName 标签名称
     * @param handlerCls 动态处理类的反射类型
     * @param suffix 后缀
     */
    protected static void add(String tagName, Class<? extends IConditHandler> handlerCls, String suffix) {
        tagHandlerMap.put(tagName, new TagHandler(handlerCls, suffix));
    }

    /**
     * 添加自定义标签和其对应的Handler class.
     * @param tagName 标签名称
     * @param prefix 前缀
     * @param handlerCls 动态处理类的反射类型
     * @param suffix 后缀
     */
    protected static void add(String tagName, String prefix,
            Class<? extends IConditHandler> handlerCls, String suffix) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerCls, suffix));
    }

    /**
     * 配置Zealot的普通配置信息.
     * @param normalConfig 普通配置实例
     */
    public abstract void configNormal(NormalConfig normalConfig);

    /**
     * 配置xml文件的标识和资源路径.
     * @param ctx xmlContext对象
     */
    public abstract void configXml(XmlContext ctx);

    /**
     * 配置标签和其对应的处理类.
     */
    public abstract void configTagHandler();

}