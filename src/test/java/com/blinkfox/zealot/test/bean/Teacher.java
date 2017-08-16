package com.blinkfox.zealot.test.bean;

/**
 * 用于测试的`Teacher`实体Bean.
 * @author blinkfox on 2017-08-16.
 */
public class Teacher {

    /** ID标识. */
    private String id;

    /** 姓名. */
    private String name;

    /** 年龄. */
    private int age;

    /** 开始日期. */
    private String startBirthday;

    /** 结束日期. */
    private String endBirthday;

    /**
     * 默认的构造方法.
     */
    public Teacher() {
        super();
    }

    /* getter 和 setter 方法. */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStartBirthday() {
        return startBirthday;
    }

    public void setStartBirthday(String startBirthday) {
        this.startBirthday = startBirthday;
    }

    public String getEndBirthday() {
        return endBirthday;
    }

    public void setEndBirthday(String endBirthday) {
        this.endBirthday = endBirthday;
    }

}