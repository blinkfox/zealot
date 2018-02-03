对于很长的动态或统计性的SQL采用Java书写不仅冗长，且不易于调试和维护，因此更推荐你通过xml文件来书写sql，使得SQL和Java代码解耦，更易于维护和阅读。使用xml方式需要经过一些配置，使系统能读取到xml中的SQL信息到缓存中，使动态拼接更高效。

### 创建Java配置类

在你的Java web项目项目中，创建一个继承自`AbstractZealotConfig`的核心配置类，如以下示例：

```java
package com.blinkfox.config;

import XmlContext;
import AbstractZealotConfig;

/**
 * 我继承的zealotConfig配置类
 * Created by blinkfox on 2016/11/4.
 */
public class MyZealotConfig extends AbstractZealotConfig {

    @Override
    public void configNormal(NormalConfig normalConfig) {
        // 1.1.5版本新增的方法
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

> (1). `configNormal()`方法是`1.1.5`版本新增的方法,主要用来配置Zealot的通用配置信息，包括是否开启`debug`模式，加载完毕之后是否打印`banner`等；

> (2). `configXml()`方法主要配置你自己SQL所在XML文件的命名标识和对应的路径，这样好让zealot能够读取到你的XML配置的SQL文件；

> (3). `configTagHandler()`方法主要是配置你自定义的标签和对应标签的处理类，当你需要自定义SQL标签时才配置。

### web.xml读取配置

然后，在你的`web.xml`中来引入zealot，这样容器启动时才会去加载和缓存对应的xml文档，示例配置如下：

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

!> **注**：如果你不是Java web项目，或者你就想通过Java代码来初始化加载zealot的配置信息，可以这样来做：

```java
ZealotConfigManager.getInstance().initLoad(MyZealotConfig.class);
```

### 创建XML的SQL文件

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

### 配置XML的命名空间

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

### 调用并生成SQL

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

```sql
--生成sql的为:
select * from user where nickname LIKE ? AND email LIKE ? AND age BETWEEN ? AND ? AND birthday BETWEEN ? AND ? AND sex in (?, ?) order by id desc

-- 生成sql的参数为:
[%张%, %san%, 23, 28, 1990-01-01 00:00:00, 1991-01-01 23:59:59, 0, 1]
```

  [3]: http://www.jfinal.com/