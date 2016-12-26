package com.blinkfox.zealot.consts;

/**
 * 常量类
 * Created by blinkfox on 2016/10/30.
 */
public final class ZealotConst {

    /* 分隔符常量,SP是Separate的简称 */
    public static final String SP_AT = "@"; // @分隔符

	/* 节点类型 */
    public static final String NODETYPE_TEXT = "Text"; // 文本节点
    public static final String NODETYPE_ELEMENT = "Element"; // 元素节点

    /* 元素节点类型 ElementType->ET */
    public static final String EQUAL = "equal";
    public static final String AND_EQUAL = "andEqual";
    public static final String OR_EQUAL = "orEqual";
    public static final String LIKE = "like";
    public static final String AND_LIKE = "andLike";
    public static final String OR_LIKE = "orLike";
    public static final String BETWEEN = "between";
    public static final String AND_BETWEEN = "andBetween";
    public static final String OR_BETWEEN = "orBetween";
    public static final String IN = "in";
    public static final String AND_IN = "andIn";
    public static final String OR_IN = "orIn";

    /* 节点相关的类型 */
    public static final String ZEALOT_TAG = "zealots/zealot";
    public static final String ATTR_CHILD = "child::node()";
    public static final String ATTR_ID = "attribute::id";
    public static final String ATTR_MATCH = "attribute::match";
    public static final String ATTR_FIELD = "attribute::field";
    public static final String ATTR_VALUE = "attribute::value";
    public static final String ATTR_START = "attribute::start";
    public static final String ATTR_ENT = "attribute::end";

    /* sql中前缀常量 */
    public static final String AND_PREFIX = " AND ";
    public static final String OR_PREFIX = " OR ";

    /* 查询sql中后缀常量 */
    public static final String EQUAL_SUFFIX = " = ? ";
    public static final String LIEK_SUFFIX = " LIKE ? ";
    public static final String GT_SUFFIX = " >= ? ";
    public static final String LT_SUFFIX = " <= ? ";
    public static final String BT_AND_SUFFIX = " BETWEEN ? AND ? ";
    public static final String IN_SUFFIX = " in ";

    /**
     * 私有构造方法
     */
    private ZealotConst() {
        super();
    }

}