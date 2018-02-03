# Zealot

[![Build Status](https://secure.travis-ci.org/blinkfox/zealot.svg)](https://travis-ci.org/blinkfox/zealot) [![codecov](https://codecov.io/gh/blinkfox/zealot/branch/master/graph/badge.svg)](https://codecov.io/gh/blinkfox/zealot) [![Maven Central](https://img.shields.io/maven-central/v/com.blinkfox/zealot.svg)](http://search.maven.org/#artifactdetails%7Ccom.blinkfox%7Czealot%7C1.2.0%7Cjar) [![Javadocs](http://www.javadoc.io/badge/com.blinkfox/zealot.svg)](http://www.javadoc.io/doc/com.blinkfox/zealot)

一个简单、强大的Java动态SQL和参数生成工具库。[文档地址](https://blinkfox.github.io/zealot/)

> My life for Auir!

![Zealot](http://i2.muimg.com/567571/faee17c016c47601.jpg)

## 一、创建初衷

SQL对开发人员来说是核心的资产之一，在开发中经常需要书写冗长、动态的SQL，许多项目中仅仅采用Java来书写动态SQL，会导致SQL分散、不易调试和阅读。所谓易于维护的SQL应该兼有动态性和可调试性的双重特性。在Java中书写冗长的SQL，虽然能很好的做到动态性，缺大大降低了SQL本身的可调试性，开发人员必须运行项目调试打印出SQL才能知道最终的SQL长什么样。所以为了做到可调试性，开发人员开始将SQL单独提取出来存放到配置文件中来维护，这样方便开发人员复制出来粘贴到SQL工具中来直接运行，但无逻辑功能的配置文件虽然解决了可调试性的问题，却又丧失了动态SQL的能力。所以，才不得不诞生出类似于mybatis这样灵活的半ORM工具来解决这两个的问题，但众多的项目却并未集成mybaits这样的工具。

[Zealot][1]是基于Java语言开发的SQL及对应参数动态拼接生成的工具包，其核心设计目标是帮助开发人员书写和生成出具有动态的、可复用逻辑的且易于维护的SQL及其对应的参数。为了做到可调试性，就必须将SQL提取到配置文件中来单独维护；为了保证SQL根据某些条件，某些逻辑来动态生成，就必须引入表达式语法或者标签语法来达到动态生成SQL的能力。因此，两者结合才诞生了Zealot。

> **注**：zealot即狂热者，是游戏[星际争霸][2]中的单位。

## 二、主要特性

- 轻量级，jar包仅仅`52`k大小，集成和使用简单
- 提供了纯`Java`代码或`XML`两种方式书写维护SQL
- `Java`的方式采用流式API的方式书写动态SQL，易于书写阅读
- `XML`的方式让SQL和Java代码解耦和，易于维护
- 具有动态性、可复用逻辑和可半调试性的优点
- 具有可扩展性，可自定义标签和处理器来完成自定义逻辑的SQL和参数生成

## 三、集成使用

### 1. 支持场景

适用于Java (web)项目，JDK1.6及以上

### 2. 安装集成

这里以Maven为例，Maven的引入方式如下：

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>zealot</artifactId>
    <version>1.2.0</version>
</dependency>
```

## 四、Java链式式之ZealotKhala

在Java中书写中等长度的SQL，用`+`连接的字符串尤其是动态字符串，会导致SQL的可读性极差且拼接性能较低，在`Zealot v1.0.4`版本中提供了一个额外高效的SQL字符串链式拼接工具Khala，但Khala只提供拼接字符串的功能，并不具有返回动态SQL和参数的特性，便决定在`v1.1.0`版本中新增了`ZealotKhala`，`ZealotKhala`类也采用流式API的方式可以书写出更流畅的动态SQL，且会得到动态SQL的有序参数。其使用示例如下：

```java
public class ZealotKhalaTest {

    /**
     * 测试使用ZealotKhala书写的sql.
     */
    @Test
    public void testSql() {
        String userName = "zhang";
        String startBirthday = "1990-03-25";
        String endBirthday = "2010-08-28";
        Integer[] sexs = new Integer[]{0, 1};

        SqlInfo sqlInfo = ZealotKhala.start()
                .select("u.id, u.name, u.email, d.birthday, d.address")
                .from("user AS u")
                .leftJoin("user_detail AS d").on("u.id = d.user_id")
                .where("u.id != ''")
                .andLike("u.name", userName)
                .doAnything(true, new ICustomAction() {
                    @Override
                    public void execute(final StringBuilder join, final List<Object> params) {
                        join.append("abc111");
                        params.add(5);
                        log.info("执行了自定义操作，可任意拼接字符串和有序参数...");
                    }
                })
                .andMoreThan("u.age", 21)
                .andLessThan("u.age", 13)
                .andMoreEqual("d.birthday", startBirthday)
                .andLessEqual("d.birthday", endBirthday)
                .andBetween("d.birthday", startBirthday, endBirthday)
                .andIn("u.sex", sexs)
                .orderBy("d.birthday").desc()
                .end();
        String sql = sqlInfo.getSql();
        Object[] arr = sqlInfo.getParamsArr();

        // 断言并输出sql信息
        assertEquals("SELECT u.id, u.name, u.email, d.birthday, d.address FROM user AS u "
                + "LEFT JOIN user_detail AS d ON u.id = d.user_id WHERE u.id != '' AND u.name LIKE ? "
                + "abc111 AND u.age > ? AND u.age < ? AND d.birthday >= ? AND d.birthday <= ? "
                + "AND d.birthday BETWEEN ? AND ? AND u.sex in (?, ?) ORDER BY d.birthday DESC", sql);
        assertArrayEquals(new Object[]{"%zhang%", 5, 21, 13, "1990-03-25", "2010-08-28",
                "1990-03-25", "2010-08-28", 0, 1} ,arr);
        log.info("testSql()方法生成的sql信息:" + sql + "\n参数为:" + Arrays.toString(arr));
    }

}
```

打印结果如下：

```sql
testSql()方法生成的sql信息:SELECT u.id, u.name, u.email, d.birthday, d.address FROM user AS u LEFT JOIN user_detail AS d ON u.id = d.user_id WHERE u.id != '' AND u.name LIKE ? abc111 AND u.age > ? AND u.age < ? AND d.birthday >= ? AND d.birthday <= ? AND d.birthday BETWEEN ? AND ? AND u.sex in (?, ?) ORDER BY d.birthday DESC
参数为:[%zhang%, 5, 21, 13, 1990-03-25, 2010-08-28, 1990-03-25, 2010-08-28, 0, 1]
```

## 五、XML方式之Zealot

对于很长的动态或统计性的SQL采用Java书写会不易于维护和调试，因此更推荐你通过xml文件来书写sql，使得SQL和Java代码解耦，易于维护和阅读。

### 配置使用

在你的`Java web`项目项目中，创建一个继承自`AbstractZealotConfig`的核心配置类，如以下示例：

```java
package com.blinkfox.config;

import com.blinkfox.zealot.config.AbstractZealotConfig;
import com.blinkfox.zealot.config.entity.NormalConfig;
import com.blinkfox.zealot.config.entity.XmlContext;

/**
 * 我继承的zealotConfig配置类
 * Created by blinkfox on 2016/11/4.
 */
public class MyZealotConfig extends AbstractZealotConfig {

    @Override
    public void configNormal(NormalConfig normalConfig) {

    }

    @Override
    public void configXml(XmlContext ctx) {

    }

    @Override
    public void configTagHandler() {

    }

}
```

> **代码解释**：

> (1). `configNormal()`方法是`1.1.5`版本新增的实现,主要用来配置Zealot的通用配置信息，包括是否开启`debug`模式，加载完毕之后是否打印`banner`等；

> (2). `configXml()`方法主要配置你自己SQL所在XML文件的命名标识和对应的路径，这样好让zealot能够读取到你的XML配置的SQL文件；

> (3). `configTagHandler()`方法主要是配置你自定义的标签和对应标签的处理类，当你需要自定义SQL标签时才配置。

然后，在你的web.xml中来引入zealot，这样容器启动时才会去加载和缓存对应的xml文档，示例配置如下：

```xml
<!-- zealot相关配置的配置 -->
<context-param>
   <!-- paramName必须为zealotConfigClass名称，param-value对应刚创建的Java配置的类路径 -->
   <param-name>zealotConfigClass</param-name>
   <param-value>com.blinkfox.config.MyZealotConfig</param-value>
</context-param>
<!-- listener-class必须配置，JavaEE容器启动时才会执行 -->
<listener>
   <listener-class>com.blinkfox.zealot.loader.ZealotConfigLoader</listener-class>
</listener>
```

如果你不是Java web项目，或者你就想通过Java代码来初始化加载zealot的配置信息，可以这样来做：

```java
ZealotConfigManager.getInstance().initLoad(MyZealotConfig.class);
```

接下来，就开始创建我们业务中的SQL及存放的XML文件了，在你项目的资源文件目录中，不妨创建一个管理SQL的文件夹，我这里取名为`zealotxml`，然后在`zealotxml`文件夹下创建一个名为`zealot-user.xml`的XML文件，用来表示用户操作相关SQL的管理文件。在XML中你就可以创建自己的SQL啦，这里对`user`表的两种查询，示例如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<zealots>

    <!-- 根据Id查询用户信息 -->
    <zealot id="queryUserById">
        select * from user where
        <equal field="id" value="id"/>
    </zealot>

    <!-- 根据动态条件查询用户信息 -->
    <zealot id="queryUserInfo">
        select * from user where
        <like field="nickname" value="nickName"/>
        <andLike match="?email != empty" field="email" value="email"/>
        <andBetween match="?startAge > 0 || ?endAge > 0" field="age" start="startAge" end="endAge"/>
        <andBetween match="?startBirthday != empty || ?endBirthday != empty" field="birthday" start="startBirthday" end="endBirthday"/>
        <andIn match="?sexs != empty" field="sex" value="sexs"/>
        order by id desc
    </zealot>

</zealots>
```

> **代码解释**：

> (1). `zealots`代表根节点，其下每个`zealot`表示你业务中需要书写的一个完整SQL。

> (2). 其中的`like`、`andLike`、`andBetween`、`andIn`等标签及属性表示要动态生成的sql类型，如：等值查询、模糊查询、In查询、区间查询等；

> (3). 标签中的属性match表示匹配到的条件，如果满足条件就生成该类型的SQL，不匹配就不生成，从而达到了动态生成SQL的需求，如果不写match，则表示必然生成；

> (4). field表示对应的数据库字段；

> (5). value、start、end则表示对应的参数。

> (6). `?email != empty`前面的`?`表示属性的安全访问，即使email不存在的时候也不会抛出异常，仍会返回false。更详细的使用可以参考MVEL属性安全访问的表达式语法。

回到你的Zealot核心配置类中，配置你Java代码中需要识别这个XML的标识和XML路径，我这里的示例如下：

```java
package com.blinkfox.config;

import XmlContext;
import AbstractZealotConfig;

/**
 * 我继承的zealotConfig配置类
 * Created by blinkfox on 2016/11/4.
 */
public class MyZealotConfig extends AbstractZealotConfig {

    public static final String USER_ZEALOT = "user_zealot";

    @Override
    public void configNormal(NormalConfig normalConfig) {
        normalConfig.setDebug(true) // 是否开启debug模式，默认为false
                .setPrintBanner(true) // 加载配置信息完毕后是否打印Banner，默认为true
                .setPrintSqlInfo(true); // 是否打印Sql信息，默认为true，注意日志级别为info时才打印，如果是warn、error则不打印
    }

    @Override
    public void configXml(XmlContext ctx) {
        ctx.add(USER_ZEALOT, "/zealotxml/zealot-user.xml");
    }

    @Override
    public void configTagHandler() {

    }

}
```

> **代码解释**：

> (1). `ctx.add(USER_ZEALOT, "/zealotxml/zealot-user.xml");`代码中第一个参数`USER_ZEALOT`表示的是对XML做唯一标识的自定义静态常量，第二个参数就是你创建的对应的XML的资源路径。

最后，就是一个UserController的调用测试类，这里的目的用来调用执行，测试我们前面配置书写的SQL和参数，参考代码如下：

```java
/**
 * 用户信息相关的控制器
 * Created by blinkfox on 16/7/24.
 */
public class UserController extends Controller {

    public void queryUserById() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", "2");

        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserById", paramMap);
        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();

        List<User> users = User.userDao.find(sql, params);
        renderJson(users);
    }

    public void userZealot() {
        // 构造测试需要的动态查询的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("nickName", "张");
        paramMap.put("email", "san");
        paramMap.put("startAge", 23);
        paramMap.put("endAge", 28);
        paramMap.put("startBirthday", "1990-01-01 00:00:00");
        paramMap.put("endBirthday", "1991-01-01 23:59:59");
        paramMap.put("sexs", new Integer[]{0, 1});

        // 执行Zealot方法，得到完整的SQL和对应的有序参数
        SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserInfo", paramMap);
        String sql = sqlInfo.getSql();
        Object[] params = sqlInfo.getParamsArr();

        // 执行SQL查询并返回结果
        List<User> users = User.userDao.find(sql, params);
        renderJson(users);
    }

}
```

> **代码解释**：

> (1). 说明一下，我测试项目中采用的框架是[JFinal][3]，你自己具体的项目中有自己的SQL调用方式，而Zealot的目的只是生成SQL和对应的有序参数而已。

> (2). Zealot.getSqlInfo()方法有三个参数，第一个参数表示前面所写的XML的标识名称，第二个表示你XML中具体想生成的SQL的zealot id,第三个表示生成动态SQL的参数对象，该对象可以是一个普通的Java对象，也可以是Map等。

结果，第二个方法userZealot()中生成的SQL和参数打印如下：

> **生成SQL结果**：

> ----生成sql的为:select * from user where nickname LIKE ? AND email LIKE ? AND age BETWEEN ? AND ? AND birthday BETWEEN ? AND ? AND sex in (?, ?) order by id desc

> ----生成sql的参数为:[%张%, %san%, 23, 28, 1990-01-01 00:00:00, 1991-01-01 23:59:59, 0, 1]

## 六、Zealot SQL配置

Zealot的核心功能就在于它XML格式的 SQL配置文件。配置文件也仅仅是一个普通的XML文件，在XML中只需要少许的配置就可以动态生成自己所需要的查询条件。在XML中`zealots`标签作为根标签，其中的`zealot`则是一个独立SQL的元素标签，在`zealot`标签中才包含`like`、`andLike`、`andBetween`、`andIn`、`text`、`import`、`choose`等条件标签,以下重点介绍各条件标签。

Zealot中默认自带了以下几类条件标签，分别是：`equal`、`like`、`between`、`in`、`text`、`import`、`choose`，分别对应着SQL查询中的等值匹配条件、模糊匹配条件、区间匹配条件、范围匹配条件及其他逻辑操作条件；某些条件标签又各自额外附带了两个连接前缀，分别是：`and`和`or`，用于表示逻辑`与`和`或`的情形。各标签的属性和生成SQL的示例如下：

### 1. equal、andEqual、orEqual 标签介绍

#### (1). 属性介绍

- **match**，表示匹配条件。非必要（填）属性，如果不写（填）此属性，则视为必然生成此条件SQL片段；否则匹配结果为true时才生成，匹配结果为false时，不生成。匹配规则使用`MVEL2`表达式，关于[MVEL的语法文档][4]参考这里。
- **field**，表示对应数据库的字段，可以是数据库的表达式、函数等。必要（填）属性。
- **value**，表示参数值，对应Java中的名称。必要（填）属性，值也是使用`MVEL2`做解析。

#### (2). 生成示例

```markup
标签:<equal field="nickname" value="nickName"></equal>
SQL片段的生成结果：nickname = ?
解释：必然生成此条SQL片段和参数

<andEqual match="?email != empty" field="email" value="email"></andEqual>
SQL片段的生成结果：AND email = ?
解释：如果email不等于空时，才生成此条SQL片段和参数
```

#### (3). 与Equal类似的标签

- moreThan 大于
- andMoreThan 带and关键字的大于
- orMoreThan 带or关键字的大于
- lessThan 小于
- andLessThan 带and关键字的小于
- orLessThan 带or关键字的小于
- moreEqual 大于等于
- andMoreEqual 带and关键字的大于等于
- orMoreEqual 带or关键字的大于等于
- lessEqual 小于等于
- andLessEqual 带and关键字的小于等于
- orLessEqual 带or关键字的小于等于

### 2. like、andLike、orLike 标签介绍

#### (1). 属性介绍

- **match**，同上。
- **field**，同上。
- **value**，同上。

#### (2). 生成示例

```markup
<andLike match="?email != empty" field="email" value="email"></andLike>

SQL片段的生成结果：AND email LIKE ?

解释：如果email不等于空时，才生成此条SQL片段和参数
```

### 3. between、andBetween、orBetween 标签介绍

#### (1). 属性介绍

- **match**，同上。
- **field**，同上。
- **start**，表示区间匹配条件的开始参数值，对应Java中的名称，条件必填。
- **end**，表示区间匹配条件的结束参数值，对应Java中的名称，条件必填。

> **注意**：Zealot中对start和end的空判断是检测是否是null,而不是空字符串，0等情况。所以，对start和end的空处理应该是null。

#### (2). 生成示例

```markup
<andBetween match="?startAge != null || ?endAge != null" field="age" start="startAge" end="endAge"></andBetween>

start为null,end不为null，则SQL片段的生成结果：AND age >= ?
start不为null,end为null，则SQL片段的生成结果：AND age <= ?
start不为null,end不为null，则SQL片段的生成结果：AND age BETWEEN ? AND ?
start为null,end为null，则不生成SQL片段

**解释**：match标签是非必填的，区间查询中，靠start和end两种条件也可以组成一个简单的动态情形。如果start为空，end不为空，则是大于等于查询；如果start为空，end不为空，则是小于等于查询；如果start、end均不为空，则是区间查询；两者会均为空则不生产此条sql。
```

### 4. in、andIn、orIn 标签介绍

#### (1). 属性介绍

- **match**，同上。
- **field**，同上。
- **value**，表示参数的集合，值可以是数组，也可以是Collection集合，还可以是单个的值。必填

#### (2). 使用生成示例

```markup
<andIn match="?sexs != empty" field="sex" value="sexs"></andIn>

SQL片段的生成结果：AND sex in (?, ?)

解释：如果sexs不等于空时，才生成此条SQL片段和参数(这里的sexs假设有两个值)
```

### 5. text 标签介绍

text标签主要用于在标签内部自定义需要的文本和需要传递的各种参数，为SQL书写提供灵活性

#### (1). 属性介绍

- **match**，同上。
- **value**，表示参数的集合，值可以是数组，也可以是Collection集合，还可以是单个的值。必填

#### (2). 使用生成示例

```markup
<text match="" value="{name1, name2, email}">
    and name in (?, ?)
    and email = ?
</text>

SQL片段的生成结果：and name in (?, ?) and email = ?

解释：如果match为true、不填写或无match标签时，才生成此条SQL片段和自定义传递的参数，参数就是通过`name1`、`name2`和`email`组合成的数组或集合，或者直接传递集合或数组（此处组合而成的数组，如果是集合就把'{'换成'['即可）。
```

### 6. import 介绍

import标签主要用于在zealot标签中导入其它公共的zealot节点，便于程序代码逻辑的复用。

#### (1). 标签

```xml
<import zealotid="" />
<import match="" zealotid="" />
<import match="" namespace="" zealotid="" value="" />
```

#### (2). 属性介绍

- **match**，表示匹配条件。非必要（填）属性，如果不写（填）此属性，则视为必然生成此条件SQL片段；否则匹配结果为true时才生成，匹配结果为false时，不生成。
- **namespace**，表示需要引用导入的节点所在的xml文件的命名空间，非必填属性。如果如果不写（填）此属性，则视为仅在本xml文件中查找对应的zealotId的节点。
- **zealotid**，表示要引用导入的zealot节点的ID，必填属性。
- **value**，表示需要传入到要引用的zealot节点中的上下文参数值，非必填属性。如果不写（填）此属性，则传递最顶层的上下文参数。

#### (3). 使用生成示例

```xml
<zealot id="commonStuCondition">
    <andMoreEqual match="?age > 0" field="s.n_age" value="age"/>
    <andBetween match="(?startBirthday != null) || (?endBirthday != null)" field="s.d_birthday" start="startBirthday" end="endBirthday"/>
</zealot>

<zealot id="queryStudents">
    ...
    <import zealotid="commonStuCondition" />
    ...
</zealot>
```

```markup
SQL片段的生成结果：AND s.n_age >= ? AND s.d_birthday BETWEEN ? AND ?
```

### 7. choose 标签介绍

choose标签主要用于解决"无数的"多分支条件选择逻辑，对应的即是Java中`if/else if/ ... /else if/else`这种逻辑。

#### (1). 标签

```xml
<choose when="" then="" when2="" then2="" ... whenx="" thenx="" else="" />
```

#### (2). 属性介绍

- **when**，表示匹配条件，可以写无数个，对应于Java中的`if/else if`条件。必要（填）属性，如果不写（填）此属性，表示false，直接进入`else`的逻辑块中。
- **then**，表示需要执行的逻辑，和`when`向对应，可以写无数个，内容是字符串或者zealot的字符串模版，必要（填）属性。如果如果不写（填）此属性，即使满足了对应的`when`条件，也不会做SQL的拼接操作。
- **else**，表示所有when条件都不满足时才执行的逻辑，内容是字符串或者zealot的字符串模版，非必填属性。如果不写（填）此属性，则表示什么都不做（这样就无任何意义了）。

#### (3). 使用生成示例

```xml
<zealot id="queryByChoose">
    UPDATE t_student SET s.c_sex =
    <choose when="?sex == 0" then="'female'" when2="?sex == 1" then2="'male'" else="unknown" />
    , s.c_status =
    <choose when="?state" then="'yes'" else="'no'" />
    , s.c_age =
    <choose when="age > 60" then="'老年'" when2="age > 40" then2="'中年'" when3="age > 20" then3="'青年'" when4="age > 10" then4="'少年'" else="'幼年'" />
    WHERE s.c_id = '@{stuId}'
</zealot>
```

```markup
SQL片段的生成结果：UPDATE t_student SET s.c_sex = 'male' , s.c_status = 'no' , s.c_age = '幼年' WHERE s.c_id = '123'
```

## 五、自定义标签和处理器

从前面所知,条件标签是生成动态SQL和参数的核心，但是项目开发的过程中往往有更多多复杂的逻辑来生成某些SQL，甚至那些逻辑还要被多处使用到，默认的一些标签不能够满足开发需求，那么自定义自己的动态条件标签来实现就显得很重要了。所谓自定义标签和处理器就是设置自定义的标签名称、匹配条件、参数和数据库字段等,再通过自定义的处理器来控制生成SQL的逻辑，这样就可以达到生成我们需要的SQL的功能，这样的标签重大的意义在于能够最大化简化sql的书写和功能的复用。

### 1. 假设查询需求

假设user表中有id、email两个字段，后台封装了一个User的参数，其中包含userId和usermail的属性。如果userId不为空时，则根据id来等值查询；如果userId为空,usermail不为空时，则根据email来做模糊查询；此处也隐含的说明了如果userId和usermail均不为空时，仍然以id来做等值查询。对此需求查询我们仍然可以用前面的标签组合来实现。假如很多地方都需要这种逻辑的查询，那我们可以使用自定义的标签来实现和复用这种查询逻辑。

### 2. 使用方式示例

#### (1). 在XML中定义标签及属性

根据上面的查询需求，可以分析出标签属性具有有`id`、`email`两个数据库字段，userId和userEmail的两个Java参数值，可设置其标签属性分别为`idValue`和`emailValue`，因此标签为：

```markup
<zealot id="queryUserWithIdEmail">
    select * from user where
    <userIdEmail match="?userId != empty || ?userEmail != empty" idField="id" emailField="email" idValue="userId" emailValue="userEmail"></userIdEmail>
</zealot>
```

#### (2). 自定义标签处理器

在你项目的某个package中，新建一个`UserIdEmailHandler.java`的文件，并让它实现`IConditHandler`接口，细节的代码处理逻辑和注释说明如下：

```java
package com.blinkfox.handler;

import BuildSource;
import SqlInfo;
import ZealotConst;
import IConditHandler;
import ParseHelper;
import StringHelper;
import XmlNodeHelper;
import org.dom4j.Node;
import java.util.List;

/**
 * 自定义的ID和Email条件查询的SQL处理器
 * Created by blinkfox on 2016/11/11.
 */
public class UserIdEmailHandler implements IConditHandler {

    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        /* 获取拼接的参数和Zealot节点 */
        SqlInfo sqlInfo = source.getSqlInfo();
        Node node = source.getNode();

        // 获取配置的属性值,getAndCheckNodeText()方法会检测属性值是否为空，如果为空，会抛出运行时异常
        String idField = XmlNodeHelper.getAndCheckNodeText(node, "attribute::idField");
        String emailField = XmlNodeHelper.getAndCheckNodeText(node, "attribute::emailField");
        // getAndCheckNodeText()方法仅仅只获取属性的值，即使未配置或书写值，也会返回空字符串
        String idValue = XmlNodeHelper.getNodeAttrText(node, "attribute::idValue");
        String emailValue = XmlNodeHelper.getNodeAttrText(node, "attribute::emailValue");

        /* 获取match属性值,如果匹配中 字符值没有，则认为是必然生成项 */
        String matchText = XmlNodeHelper.getNodeAttrText(node, ZealotConst.ATTR_MATCH);
        if (StringHelper.isBlank(matchText)) {
            sqlInfo = buildIdEmailSqlInfo(source, idField, emailField, idValue, emailValue);
        } else {
            /* 如果match匹配成功，则生成数据库sql条件和参数 */
            Boolean isTrue = (Boolean) ParseHelper.parseWithMvel(matchText, source.getParamObj());
            if (isTrue) {
                sqlInfo = buildIdEmailSqlInfo(source, idField, emailField, idValue, emailValue);
            }
        }

        return sqlInfo;
    }

    /**
     * 构建自定义的SqlInfo信息，区分是根据id做等值查询，还是根据email做模糊查询的情况
     * @param source source
     * @param idField idField
     * @param emailField emailField
     * @param idValue idValue
     * @param emailValue emailValue
     */
    private SqlInfo buildIdEmailSqlInfo(BuildSource source, String idField, String emailField,
            String idValue, String emailValue) {
        SqlInfo sqlInfo = source.getSqlInfo();
        StringBuilder join = sqlInfo.getJoin();
        List<Object> params = sqlInfo.getParams();

        // 如果userId不为空，则根据id来做等值查询
        Integer idText = (Integer) ParseHelper.parseWithMvel(idValue, source.getParamObj());
        if (idText != null) {
            // prefix是前缀，如"and","or"之类，没有则默认为空字符串""
            join.append(source.getPrefix()).append(idField).append(ZealotConst.EQUAL_SUFFIX);
            params.add(idText);
            return sqlInfo.setJoin(join).setParams(params);
        }

        // 获取userEmail的值,如果userEmail不为空，则根据email来做模糊查询
        String emailText = (String) ParseHelper.parseWithMvel(emailValue, source.getParamObj());
        if (StringHelper.isNotBlank(emailText)) {
            // prefix是前缀，如"and","or"之类，没有则默认为空字符串""
            join.append(source.getPrefix()).append(emailField).append(ZealotConst.LIEK_SUFFIX);
            params.add("%" + emailText + "%");
            return sqlInfo.setJoin(join).setParams(params);
        }

        return sqlInfo;
    }

}
```

#### (3). 配置自定义的标签和处理器

在你继承的Zealot Java配置文件方法中添加配置自定义的标签和处理器，重启即可，代码示例如下：

```java
/**
 * 我继承的zealotConfig配置类
 * Created by blinkfox on 2016/11/4.
 */
public class MyZealotConfig extends AbstractZealotConfig {

    public static final String USER_ZEALOT = "user_zealot";

    @Override
    public void configXml(XmlContext ctx) {
        ctx.add(USER_ZEALOT, "/zealot/zealot-user.xml");
    }

    @Override
    public void configTagHandler() {
        // 自定义userIdEmail标签和处理器
        add("userIdEmail", UserIdEmailHandler.class);
        // 有and前缀的自定义标签
        add("andUserIdEmail", " and " ,UserIdEmailHandler.class);
    }

}
```

#### (4). 测试生成结果

测试代码和结果如下：

```java
public void queryUserIdEmail() {
    Map<String, Object> user = new HashMap<String, Object>();
    user.put("userId", 3);
    user.put("userEmail", "san");

    SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserWithIdEmail", user);
    String sql = sqlInfo.getSql();
    Object[] params = sqlInfo.getParamsArr();
    System.out.println("----生成sql的为:" + sql);
    System.out.println("----生成sql的参数为:" + Arrays.toString(params));

    List<User> users = User.userDao.find(sql, params);
    renderJson(users);
}
```

打印的sql结果：

```markup
----生成sql的为:select * from user where id = ?
----生成sql的参数为:[3]
```

当把userId的值设为null时，打印的sql结果：

```markup
----生成sql的为:select * from user where email LIKE ?
----生成sql的参数为:[%san%]
```

## 七、流程控制标签

由于Zealot中SQL片段生成的标签大多是工具库预设或自定义的，但是在实现更为灵活的逻辑控制时就显得力不从心了，如果都通过自定义标签去实现更灵活多变的逻辑会显得很麻烦。因此，决定在1.0.6的版本中增加更为强大灵活的流程逻辑控制标签。自由的流程逻辑控制方式就意味着难以实现绑定变量参数的方式来生成SQL，而是即时生成替换变量值后的SQL。

### 1. 使用示例

Zealot中SQL片段标签和流程控制标签是可以混合使用的，看下面的SQL书写方式即可容易理解：

```xml
<!-- 根据流程控制标签查询用户信息 -->
<zealot id="queryUsersByFlowTag">
    select * from user where
    <like field="nickname" value="nickName"/>
    @if{?email != empty}
        AND email like '%@{email}%'
    @end{}
    order by id desc
</zealot>
```

当email不为空时就会生成类似如下的SQL了:

```sql
select * from user where nickname LIKE ? AND email like '%zhang%' order by id desc
```

### 2. 常用流程控制标签介绍

Zealot的流程控制标签使用的是MVEL模板标签，所以，支持所有MVEL2.0的模板标签，这也正体现了Zealot动态SQL的强大特性。关于MVEL2.x的模板更详细的介绍请参考[这里][5]。

#### (1). @{} 表达式

@{}表达式是最基本的形式。它包含一个对字符串求值的值表达式，并附加到输出模板中。例如：

```java
Hello, my name is @{person.name}
```

#### (2). @code{} 静默代码标签

静默代码标记允许您在模板中执行MVEL表达式代码。它不返回值，并且不以任何方式影响模板的格式。

```java
@code{age = 23; name = 'John Doe'}
@{name} is @{age} years old
```
该模板将计算出：John Doe is 23 years old。

#### (3). @if{}@else{} 控制流标签

@if{}和@else{}标签在MVEL模板中提供了完全的if-then-else功能。 例如：

```java
@if{foo != bar}
   Foo not a bar!
@else{bar != cat}
   Bar is not a cat!
@else{}
   Foo may be a Bar or a Cat!
@end{}
```

MVEL模板中的所有块必须用`@end{}`标签来终止，除非是`if-then-else`结构，其中`@else{}`标记表示前一个控制语句的终止。

#### (4). @foreach{} Foreach迭代

foreach标签允许您在模板中迭代集合或数组。 注意：foreach的语法已经在MVEL模板2.0中改变，以使用foreach符号来标记MVEL语言本身的符号。

```java
@foreach{item : products}
 - @{item.serialNumber}
@end{}
```

## 八、更多功能

Zealot中除了上面介绍的一些功能之外，还有其他额外的辅助、简化开发的功能，以下作简要介绍。

### 1. 上下文参数包装器

在`v1.2.0`版本中增加了上下文参数包装器`ParamWrapper`的功能，其本质上就是对`HashMap`的封装。在以前的版本中需要自己封装`JavaBean`对象或者Map对象来作为SQL拼接的上下文参数传入：

#### (1). 前后对比的示例

以前需要开发者自己封装Map：

```java
Map<String, Object> paramMap = new HashMap<String, Object>();
paramMap.put("sex", "1")
paramMap.put("stuId", "123")
paramMap.put("state", false)

SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.STUDENT_ZEALOT, "queryByChoose", paramMap);
```

现在的使用方式：

```java
SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.STUDENT_ZEALOT, "queryByChoose",
        ParamWrapper.newInstance("sex", "1").put("stuId", "123").put("state", false).toMap());
```

前后对比来看，再仅仅只需要传入个别自定义参数时，能简化部分代码量。

#### (2). ParamWrapper主要方法

- `newInstance()`，创建新的`ParamWrapper`实例。
- `newInstance(Map<String, Object> paramMap)`，传入已有的`Map`型对象，并创建新的`ParamWrapper`实例。
- `newInstance(String key, Object value)`，创建新的`ParamWrapper`实例，并创建一对key和value的键值对。
- `put(String key, Object value)`，向参数包装器中，`put`对应的key和value值。
- `toMap()`，返回填充了key、value后的Map对象。

### 2. 表达式、模版解析器

在zealot中解析xml标签中的表达式或者模版是通过`Mvel`表达式语言来实现的，主要方法解析方法是封装在了`ParseHelper`的工具类中，通过该类让开发人员自己测试表达式也是极为方便的。以下作简要介绍。

#### (1). 解析表达式

主要方法：

- parseExpress(String exp, Object paramObj)，解析出表达式的值，如果解析出错则不抛出异常，但会输出error级别的异常，不影响zealot的后续执行。
- parseExpressWithException(String exp, Object paramObj)，解析出表达式的值，如果解析出错则抛出异常，会影响往zealot的后续执行。

使用示例：

```java
@Test
public void testParseWithMvel() {
    // 构造上下文参数
    Map<String, Object> context = new HashMap<String, Object>();
    context.put("foo", "Hello");
    context.put("bar", "World");

    String result = (String) ParseHelper.parseExpressWithException("foo + bar", context);
    assertEquals("HelloWorld", result); // 解析得到`HelloWorld`字符串，断言为:true。
}

@Test
public void testParseStr2() {
    Boolean result = (Boolean) ParseHelper.parseExpress("sex == 1", ParamWrapper.newInstance("sex", "1").toMap());
    assertEquals(true, result); // 断言为:true。
}
```

#### (2). 解析模版

主要方法：

- parseTemplate(String template, Object paramObj)，解析出表达式的值，如果解析出错则抛出异常，影响zealot的后续执行。

使用示例：

```java
@Test
public void testParseTemplate2() {
    String result = ParseHelper.parseTemplate("@if{?foo != empty}@{foo} World!@end{}", ParamWrapper.newInstance("foo", "Hello").toMap());
    assertEquals("Hello World!", result);// 解析得到`Hello World!`字符串，断言为:true。
}
```

#### (3). 真假判断

主要方法：

- isMatch(String match, Object paramObj)，是否匹配，常用于标签中的match值的解析，即如果match不填写，或者内容为空，或者解析出为正确的值，都视为`true`。
- isNotMatch(String match, Object paramObj)，是否不匹配，同`isMatch`相反，只有解析到的值是false时，才认为是false。
- isTrue(String exp, Object paramObj)，是否为true，只有当解析值确实为true时，才为true。

## 九、许可证

Zealot类库遵守[Apache License 2.0][6] 许可证

## 十、版本更新记录

- v1.2.0(2017-08-18)
  - 新增`import`标签，用于引入公共的`zealot`标签节点，便于逻辑和代码复用
  - 新增`choose`标签，可以无限制写无数的`if/else if/else`等条件选择分支逻辑，方便书写条件选择的动态SQL片段
  - 新增`ParamWrapper`工具类，方便更快捷的创建参数的上下文对象
  - 单元测试类的包结构重构
- v1.1.6(2017-07-27)
  - 将`slf4j`的日志改为了`JDK`的日志
- v1.1.5(2017-07-24)
  - 新增通用配置功能，包括debug模式、是否打印Sql信息、是否打印Banner等
  - 新增依赖了`slf4j`的日志接口，各系统引入`slf4j`的日志实现即可
  - 去掉了被标注为`@Deprecated`的过时类`ZealotKhala`
  - 一些类的代码重构和JavaDoc完善
- v1.1.4(2017-05-06)
  - 修复了启动时打印banner出错的问题
- v1.1.3(2017-05-01)
  - 新增了不等于的情况
  - 完善单元测试和代码覆盖率
- v1.1.2(2017-04-22)
  - 新增了zealot加载完成时的banner显示
  - 新增或升级了一些pom文件中的插件，如：pmd、reports等
  - 其他代码小细节修改
- v1.1.1(2017-04-16)
  - 新增了ZealotKhala和xml标签的常用API，如：大于、小于、大于等于、小于等于等功能。
  - 新增了Zealot中xml的text标签，使灵活性SQL拼接灵活性更强
  - 新增了ZealotKhala的ICustomAction接口，使自定义的逻辑也能够通过链式写法完成，使SQL拼接逻辑更紧凑
  - 标记`Khala.java`为推荐使用，即`@Deprecated`。推荐使用`ZealotKhala.java`，使SQL的动态性、灵活性更强。
- v1.1.0(2017-04-04)
  - 新增了ZealotKhala，使ZealotKhala用Java也可以链式的书写动态SQL，和Zealot的XML标签相互应
- v1.0.7(2017-03-31)
  - 使用Google CheckStyle来规范Java代码风格,重构了部分代码,使代码更整洁
  - Khala字符串的链式拼接去掉了手动newInstance的方式，直接调用start()方法即可
- v1.0.6(2016-12-31)
  - 新增灵活强大的流程逻辑控制标签
  - 新增自定义标签的示例和单元测试
- v1.0.5(2016-12-29)
  - 新增Zealot基本功能的单元测试
  - 重构Zealot缓存加载的代码
  - 新增了Khala的获取实例的方法
- v1.0.4(2016-11-12)
  - 新增了SQL字符串链式拼接工具类Khala.java
- v1.0.3(2016-11-11)
  - 修复了区间查询大于或等于情况下的bug
  - XmlNodeHelper中新增getNodeAttrText()方法
- v1.0.2(2016-11-10)
  - 将缓存文档改为缓存Zealot节点，使生成sql效率更快
  - 代码细节重构调整
- v1.0.1(2016-11-08)
  - 新增日志功能，替换System.out
  - 新增自定义异常
  - 完善文档注释
- v1.0.0(2016-11-04)
  - 核心功能完成


  [1]: https://github.com/blinkfox/zealot
  [2]: http://v.youku.com/v_show/id_XMTM4MjgyNDgxMg
  [3]: http://www.jfinal.com/
  [4]: http://mvel.documentnode.com/
  [5]: http://blinkfox.com/mvel-2-xmo-ban-zhi-nan/
  [6]: http://www.apache.org/licenses/LICENSE-2.0