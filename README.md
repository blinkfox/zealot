# Zealot

一个轻量级的SQL和参数动态生成工具库

> My life for Auir!

![Zealot](http://static.blinkfox.com/zealot.jpg)

## 一、创建初衷

SQL对开发人员来说是核心的资产之一，在开发中经常需要书写冗长、动态的SQL，许多项目中仅仅采用Java来书写动态SQL，会导致SQL分散、不易调试和阅读。所谓易于维护的SQL应该兼有动态性和可调试性的双重特性。在Java中书写冗长的SQL，虽然能很好的做到动态性，缺大大降低了SQL本身的可调试性，开发人员必须运行项目调试打印出SQL才能知道最终的SQL长什么样。所以为了做到可调试性，开发人员开始将SQL单独提取出来存放到配置文件中来维护，这样方便开发人员复制出来粘贴到SQL工具中来直接运行，但无逻辑功能的配置文件虽然解决了可调试性的问题，却又丧失了动态SQL的能力。所以，才不得不诞生出类似于mybatis这样灵活的半ORM工具来解决这两个的问题，但众多的项目却并未集成mybaits这样的工具。

[Zealot][1]是基于Java语言开发的SQL及对应参数动态拼接生成的工具包，其核心设计目标是帮助开发人员书写和生成出具有动态的、可复用逻辑的且易于维护的SQL及其对应的参数。为了做到可调试性，就必须将SQL提取到配置文件中来单独维护；为了保证SQL根据某些条件，某些逻辑来动态生成，就必须引入表达式语法或者标签语法来达到动态生成SQL的能力。因此，两者结合才诞生了Zealot。

## 二、主要特性

- 轻量级，jar包仅仅27k大小，集成和使用简单
- 让SQL和Java代码解耦和，易于维护
- SQL采用XML集中管理，同时方便程序开发
- 具有动态性、可复用逻辑和可半调试性的优点
- 具有可扩展性，可自定义标签和处理器来完成自定义逻辑的SQL和参数生成

## 三、集成使用

### 1. 支持场景

适用于Java web项目，JDK1.6及以上

### 2. 安装集成

这里以Maven为例，Maven的引入方式如下：

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>zealot</artifactId>
    <version>1.0.3</version>
</dependency>
```

### 3. 配置使用

在你的Java web项目项目中，创建一个继承自AbstractZealotConfig的核心配置类，如以下示例：

```java
package com.blinkfox.config;

import com.blinkfox.zealot.bean.XmlContext;
import com.blinkfox.zealot.config.AbstractZealotConfig;

/**
 * 我继承的zealotConfig配置类
 * Created by blinkfox on 2016/11/4.
 */
public class MyZealotConfig extends AbstractZealotConfig {

    @Override
    public void configXml(XmlContext ctx) {
        
    }

    @Override
    public void configTagHandler() {

    }

}
```

> **代码解释**：

> (1). configXml()方法主要配置你自己SQL所在XML文件的命名标识和对应的路径，这样好让zealot能够读取到你的XML配置的SQL文件；

> (2). configTagHandler()方法主要是配置你自定义的标签和对应标签的处理类，当你需要自定义SQL标签时才配置。

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

接下来，就开始创建我们业务中的SQL及存放的XML文件了，在你项目的资源文件目录中，不妨创建一个管理SQL的文件夹，我这里取名为`zealotxml`，然后在`zealotxml`文件夹下创建一个名为`zealot-user.xml`的XML文件，用来表示用户操作相关SQL的管理文件。在XML中你就可以创建自己的SQL啦，这里对`user`表的两种查询，示例如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<zealots>

    <!-- 根据Id查询用户信息 -->
    <zealot id="queryUserById">
        select * from user where
        <equal field="id" value="id"></equal>
    </zealot>
    
    <!-- 根据动态条件查询用户信息 -->
    <zealot id="queryUserInfo">
        select * from user where
        <like field="nickname" value="nickName"></like>
        <andLike match="email != empty" field="email" value="email"></andLike>
        <andBetween match="startAge > 0 || endAge > 0" field="age" start="startAge" end="endAge"></andBetween>
        <andBetween match="startBirthday != empty || endBirthday != empty" field="birthday" start="startBirthday" end="endBirthday"></andBetween>
        <andIn match="sexs != empty" field="sex" value="sexs"></andIn>
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

回到你的Zealot核心配置类中，配置你Java代码中需要识别这个XML的标识和XML路径，我这里的示例如下：

```java
package com.blinkfox.config;

import com.blinkfox.zealot.bean.XmlContext;
import com.blinkfox.zealot.config.AbstractZealotConfig;

/**
 * 我继承的zealotConfig配置类
 * Created by blinkfox on 2016/11/4.
 */
public class MyZealotConfig extends AbstractZealotConfig {

    public static final String USER_ZEALOT = "user_zealot";

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

> (1). 说明一下，我测试项目中采用的框架是[JFinal][2]，你自己具体的项目中有自己的SQL调用方式，而Zealot的目的只是生成SQL和对应的有序参数而已。

> (2). Zealot.getSqlInfo()方法有三个参数，第一个参数表示前面所写的XML的标识名称，第二个表示你XML中具体想生成的SQL的zealot id,第三个表示生成动态SQL的参数对象，该对象可以是一个普通的Java对象，也可以是Map等。

结果，第二个方法userZealot()中生成的SQL和参数打印如下：

> **生成SQL结果**：

> ----生成sql的为:select * from user where nickname LIKE ? AND email LIKE ? AND age BETWEEN ? AND ? AND birthday BETWEEN ? AND ? AND sex in (?, ?) order by id desc

> ----生成sql的参数为:[%张%, %san%, 23, 28, 1990-01-01 00:00:00, 1991-01-01 23:59:59, 0, 1]

## 四、Zealot SQL配置

Zealot的核心功能就在于它XML格式的 SQL配置文件。配置文件也仅仅是一个普通的XML文件，在XML中只需要少许的配置就可以动态生成自己所需要的查询条件。在XML中`zealots`标签作为根标签，其中的`zealot`则是一个独立SQL的元素标签，在`zealot`标签中才包含`like`、`andLike`、`andBetween`、`andIn`等条件标签,以下重点介绍各条件标签。

Zealot中默认自带了以下4类条件标签，分别是：`equal`、`like`、`between`、`in`，分别对应着SQL查询中的等值匹配条件、模糊匹配条件、区间匹配条件以及范围匹配条件；四类条件标签又各自额外附带了两个连接前缀，分别是：`and`和`or`，用于表示逻辑`与`和`或`的情形，这两者更为常用，目前还未加入`非`的情形。所以，zealot中总共带有12个条件标签，各标签的属性和生成SQL的示例如下：

### 1. equal、andEqual、orEqual 标签介绍

#### (1). 属性介绍

- **match**，表示匹配条件。非必要（填）属性，如果不写（填）此属性，则视为必然生成此条件SQL片段；否则匹配结果为true时才生成，匹配结果为false时，不生成。匹配规则使用`MVEL2`表达式，关于[MVEL的语法文档][3]参考这里。
- **field**，表示对应数据库的字段，可以是数据库的表达式、函数等。必要（填）属性。
- **value**，表示参数值，对应Java中的名称。必要（填）属性，值也是使用`MVEL2`做解析。

#### (2). 生成示例

```markup
标签:<equal field="nickname" value="nickName"></equal>
SQL片段的生成结果：nickname = ?
解释：必然生成此条SQL片段和参数

<andEqual match="email != empty" field="email" value="email"></andEqual>
SQL片段的生成结果：AND email = ?
解释：如果email不等于空时，才生成此条SQL片段和参数
```

### 2. like、andLike、orLike 标签介绍

#### (1). 属性介绍

- **match**，同上。
- **field**，同上。
- **value**，同上。

#### (2). 生成示例

```markup
<andLike match="email != empty" field="email" value="email"></andLike>

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
<andBetween match="startAge != null || endAge != null" field="age" start="startAge" end="endAge"></andBetween>

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
- **value**，表示参数的集合，值可以是数组，也可以是Collection集合。必填

#### (2). 使用生成示例

```markup
<andIn match="sexs != empty" field="sex" value="sexs"></andIn>

SQL片段的生成结果：AND sex in (?, ?)

解释：如果sexs不等于空时，才生成此条SQL片段和参数(这里的sexs假设有两个值)
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
    <userIdEmail match="userId != empty || userEmail != empty" idField="id" emailField="email" idValue="userId" emailValue="userEmail"></userIdEmail>
</zealot>
```

#### (2). 自定义标签处理器

在你项目的某个package中，新建一个`UserIdEmailHandler.java`的文件，并让它实现`IConditHandler`接口，细节的代码处理逻辑和注释说明如下：

```java
package com.blinkfox.handler;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.IConditHandler;
import com.blinkfox.zealot.helpers.ParseHelper;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.helpers.XmlNodeHelper;
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
     * @param source
     * @param idField
     * @param emailField
     * @param idValue
     * @param emailValue
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

```
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

## 六、许可证

Zealot类库遵守[Apache License 2.0][4] 许可证

## 七、版本更新记录

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
  [2]: http://www.jfinal.com/
  [3]: http://mvel.documentnode.com/
  [4]: http://www.apache.org/licenses/LICENSE-2.0