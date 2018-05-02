package com.blinkfox.zealot.test.helpers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.helpers.CollectionHelper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 集合工具类的单元测试类.
 * @author blinkfox on 2017/4/29.
 */
public class CollectionHelperTest {

    /**
     * 测试是否不为空的方法.
     */
    @Test
    public void testIsNotEmpty() {
        assertEquals(false, CollectionHelper.isNotEmpty(null));
        assertEquals(false, CollectionHelper.isNotEmpty(new Object[]{}));
        assertEquals(true, CollectionHelper.isNotEmpty(new Object[]{1}));
    }

    /**
     * 测试是否不为空的方法.
     */
    @Test
    public void testCollectionIsEmpty() {
        List<String> names = new ArrayList<String>();
        assertEquals(true, CollectionHelper.isEmpty(null));
        assertEquals(true, CollectionHelper.isEmpty(names));
        names.add("Tom");
        assertEquals(false, CollectionHelper.isEmpty(names));
    }

    /**
     * 测试转换为数组的方法.
     */
    @Test
    public void testToArray() {
        assertArrayEquals(new Object[]{3},
                CollectionHelper.toArray(3, 0));
        assertArrayEquals(new Object[]{1, 2},
                CollectionHelper.toArray(new Object[]{1, 2}, ZealotConst.OBJTYPE_ARRAY));

        List<String> lists = new ArrayList<String>();
        lists.add("hello");
        lists.add("world");
        assertArrayEquals(new Object[]{"hello", "world"},
                CollectionHelper.toArray(lists, ZealotConst.OBJTYPE_COLLECTION));
    }

}