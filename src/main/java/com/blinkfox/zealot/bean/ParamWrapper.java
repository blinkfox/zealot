package com.blinkfox.zealot.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Zealot上下文参数的包装器.
 * @author blinkfox on 2017-08-16.
 */
public final class ParamWrapper {

    /** 封装参数的一个map. */
    private Map<String, Object> paramMap;

    /**
     * 获取paramMap的Map对象.
     * @return paramMap
     */
    public Map<String, Object> toMap() {
        return paramMap;
    }

    /**
     * 默认构造方法.
     */
    public ParamWrapper() {
        this.paramMap = new HashMap<String, Object>();
    }

    /**
     * 构造第一个key和value来初始化map中数据的构造方法.
     * @param key 键
     * @param value 值
     */
    public ParamWrapper(String key, Object value) {
        this.paramMap = new HashMap<String, Object>();
        this.paramMap.put(key, value);
    }

    /**
     * 存放参数的key和value值.
     * @param key 键
     * @param value 值
     * @return map
     */
    public Map<String, Object> put(String key, Object value) {
        this.paramMap.put(key, value);
        return paramMap;
    }

}