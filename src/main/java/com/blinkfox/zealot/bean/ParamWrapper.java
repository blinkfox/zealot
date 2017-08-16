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
    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    /**
     * 私有构造方法.
     */
    private ParamWrapper(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 获取新的ParamWrapper实例.
     * @return ParamWrapper实例
     */
    public static ParamWrapper newInstance() {
        return new ParamWrapper(new HashMap<String, Object>());
    }

    /**
     * 存放参数的key和value值.
     * @param key 键
     * @param value 值
     * @return map
     */
    public Map<String, Object> put(String key, Object value) {
        paramMap.put(key, value);
        return paramMap;
    }

}