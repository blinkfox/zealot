package com.blinkfox.zealot.consts;

/**
 * 常量类.
 * Created by blinkfox on 2016/10/30.
 */
public final class ZealotConst {

    /** 分隔符常量,SP是Separate的简称. */
    public static final String SP_AT = "@@"; // @@分隔符

    /* 节点类型 */
    public static final String NODETYPE_TEXT = "Text"; // 文本节点
    public static final String NODETYPE_ELEMENT = "Element"; // 元素节点

    /* 自定义的zealot的元素节点类型. */
    // 等于
    public static final String EQUAL = "equal";
    public static final String AND_EQUAL = "andEqual";
    public static final String OR_EQUAL = "orEqual";
    // 不等于
    public static final String NOT_EQUAL = "notEqual";
    public static final String AND_NOT_EQUAL = "andNotEqual";
    public static final String OR_NOT_EQUAL = "orNotEqual";
    // 大于
    public static final String MORE = "moreThan";
    public static final String AND_MORE = "andMoreThan";
    public static final String OR_MORE = "orMoreThan";
    // 小于
    public static final String LESS = "lessThan";
    public static final String AND_LESS = "andLessThan";
    public static final String OR_LESS = "orLessThan";
    // 大于等于
    public static final String MORE_EQUAL = "moreEqual";
    public static final String AND_MORE_EQUAL = "andMoreEqual";
    public static final String OR_MORE_EQUAL = "orMoreEqual";
    // 小于等于
    public static final String LESS_EQUAL = "lessEqual";
    public static final String AND_LESS_EQUAL = "andLessEqual";
    public static final String OR_LESS_EQUAL = "orLessEqual";
    // like模糊
    public static final String LIKE = "like";
    public static final String AND_LIKE = "andLike";
    public static final String OR_LIKE = "orLike";
    // between区间
    public static final String BETWEEN = "between";
    public static final String AND_BETWEEN = "andBetween";
    public static final String OR_BETWEEN = "orBetween";
    // in范围
    public static final String IN = "in";
    public static final String AND_IN = "andIn";
    public static final String OR_IN = "orIn";
    // text文本
    public static final String TEXT = "text";
    // import导入文本
    public static final String IMPORT = "import";
    // choose文本
    public static final String CHOOSE = "choose";

    /* 节点相关的类型 */
    public static final String ZEALOT_TAG = "zealots/zealot";
    public static final String ATTR_CHILD = "child::node()";
    public static final String ATTR_ID = "attribute::id";
    public static final String ATTR_MATCH = "attribute::match";
    public static final String ATTR_FIELD = "attribute::field";
    public static final String ATTR_VALUE = "attribute::value";
    public static final String ATTR_START = "attribute::start";
    public static final String ATTR_ENT = "attribute::end";
    public static final String ATTR_NAME_SPACE = "attribute::namespace";
    public static final String ATTR_ZEALOT_ID = "attribute::zealotid";
    public static final String ATTR_WHEN = "attribute::when";
    public static final String ATTR_THEN = "attribute::then";
    public static final String ATTR_ELSE = "attribute::else";

    /* sql中前缀常量 */
    public static final String ONE_SPACE = " ";
    public static final String AND_PREFIX = " AND ";
    public static final String OR_PREFIX = " OR ";

    /* sql中的后缀常量 */
    public static final String EQUAL_SUFFIX = " = ? ";
    public static final String GT_SUFFIX = " > ? ";
    public static final String LT_SUFFIX = " < ? ";
    public static final String GTE_SUFFIX = " >= ? ";
    public static final String LTE_SUFFIX = " <= ? ";
    public static final String NOT_EQUAL_SUFFIX = " <> ? ";
    public static final String LIEK_KEY = " LIKE ";
    public static final String NOT_LIEK_KEY = " NOT LIKE ";
    public static final String LIEK_SUFFIX = " LIKE ? ";
    public static final String NOT_LIEK_SUFFIX = " NOT LIKE ? ";
    public static final String BT_AND_SUFFIX = " BETWEEN ? AND ? ";
    public static final String IN_SUFFIX = " IN ";

    /* 集合类型的常量,0表示单个对象，1表示普通数组，2表示Java集合 */
    public static final int OBJTYPE_ARRAY = 1;
    public static final int OBJTYPE_COLLECTION = 2;

    /**
     * 私有构造方法.
     */
    private ZealotConst() {
        super();
    }

}