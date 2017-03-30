package com.blinkfox.zealot.config;

import com.blinkfox.zealot.bean.TagHandler;
import com.blinkfox.zealot.bean.XmlContext;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.concrete.BetweenHandler;
import com.blinkfox.zealot.core.concrete.EqualHandler;
import com.blinkfox.zealot.core.concrete.InHandler;
import com.blinkfox.zealot.core.concrete.LikeHandler;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.dom4j.Node;

/**
 * Zealot主配置类.
 * Created by blinkfox on 2016/10/30.
 */
public abstract class AbstractZealotConfig {

    // 所有zealots XML文档的缓存map，
    // key是资源的路径（将xml命名空间和zealotId用"@"符号分割），value是dom4j文档节点Node
    private static Map<String, Node> zealots = new ConcurrentHashMap<String, Node>();

    // 初始化默认的一些tagHandlers到HashSet集合中
    private static Set<TagHandler> tagHandlers = new HashSet<TagHandler>();
    
    /* 添加默认的标签和对应的handler处理器，分别是equal,like,between,in等 */
    static {
        add(ZealotConst.EQUAL, EqualHandler.class);
        add(ZealotConst.AND_EQUAL, ZealotConst.AND_PREFIX, EqualHandler.class);
        add(ZealotConst.OR_EQUAL, ZealotConst.OR_PREFIX, EqualHandler.class);
        add(ZealotConst.LIKE, LikeHandler.class);
        add(ZealotConst.AND_LIKE, ZealotConst.AND_PREFIX, LikeHandler.class);
        add(ZealotConst.OR_LIKE, ZealotConst.OR_PREFIX, LikeHandler.class);
        add(ZealotConst.BETWEEN, BetweenHandler.class);
        add(ZealotConst.AND_BETWEEN, ZealotConst.AND_PREFIX, BetweenHandler.class);
        add(ZealotConst.OR_BETWEEN, ZealotConst.OR_PREFIX, BetweenHandler.class);
        add(ZealotConst.IN, InHandler.class);
        add(ZealotConst.AND_IN, ZealotConst.AND_PREFIX, InHandler.class);
        add(ZealotConst.OR_IN, ZealotConst.OR_PREFIX, InHandler.class);
    }

    /**
     * 获取全局的Zealots文档缓存数据.
     * @return 返回xml命名空间和dom4j文件的缓存map
     */
    public static Map<String, Node> getZealots() {
        return zealots;
    }

    /**
     * 获取全局的tagHandlers集合对象.
     * @return 返回标签和其Handler的对象
     */
    public static Set<TagHandler> getTagHandlers() {
        return tagHandlers;
    }

    /**
     * 添加自定义标签和其对应的Handler class.
     * @param tagName 标签名称
     * @param handlerCls 动态处理类的反射类型
     */
    protected static void add(String tagName, Class<?> handlerCls) {
        tagHandlers.add(new TagHandler(tagName, handlerCls));
    }

    /**
     * 添加自定义标签和其对应的Handler class.
     * @param tagName 标签名称
     * @param prefix 前缀
     * @param handlerCls 动态处理类的反射类型
     */
    protected static void add(String tagName, String prefix, Class<?> handlerCls) {
        tagHandlers.add(new TagHandler(tagName, prefix, handlerCls));
    }

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