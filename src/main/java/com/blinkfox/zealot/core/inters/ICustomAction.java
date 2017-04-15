package com.blinkfox.zealot.core.inters;

import java.util.List;

/**
 * 执行自定义任意操作的接口.
 * Created by blinkfox on 2017/4/15.
 */
public interface ICustomAction {

    /**
     * 执行的执行语句.
     * @param join 拼接SQL字符串的StringBuilder对象
     * @param params 有序参数
     */
    void execute(final StringBuilder join, final List<Object> params);

}