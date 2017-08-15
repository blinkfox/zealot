从前面所知,条件标签是生成动态SQL和参数的核心，但是项目开发的过程中往往有更多多复杂的逻辑来生成某些SQL，甚至那些逻辑还要被多处使用到，默认的一些标签不能够满足开发需求，那么自定义自己的动态条件标签来实现就显得很重要了。所谓自定义标签和处理器就是设置自定义的标签名称、匹配条件、参数和数据库字段等,再通过自定义的处理器来控制生成SQL的逻辑，这样就可以达到生成我们需要的SQL的功能，这样的标签重大的意义在于能够最大化简化sql的书写和功能的复用。

### 假设查询需求

假设user表中有id、email两个字段，后台封装了一个User的参数，其中包含userId和usermail的属性。如果userId不为空时，则根据id来等值查询；如果userId为空,usermail不为空时，则根据email来做模糊查询；此处也隐含的说明了如果userId和usermail均不为空时，仍然以id来做等值查询。对此需求查询我们仍然可以用前面的标签组合来实现。假如很多地方都需要这种逻辑的查询，那我们可以使用自定义的标签来实现和复用这种查询逻辑。

### 使用方式示例

#### 在XML中定义标签及属性

根据上面的查询需求，可以分析出标签属性具有有`id`、`email`两个数据库字段，userId和userEmail的两个Java参数值，可设置其标签属性分别为`idValue`和`emailValue`，因此标签为：

```markup
<zealot id="queryUserWithIdEmail">
    select * from user where
    <userIdEmail match="?userId != empty || ?userEmail != empty" idField="id" emailField="email" idValue="userId" emailValue="userEmail"></userIdEmail>
</zealot>
```

#### 自定义标签处理器

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

#### 配置自定义的标签和处理器

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

####  测试生成结果

测试代码和结果如下：

```
public void queryUserIdEmail() {
    Map<String, Object> user = new HashMap<String, Object>();
    user.put("userId", 3);
    user.put("userEmail", "san");

    SqlInfo sqlInfo = Zealot.getSqlInfo(MyZealotConfig.USER_ZEALOT, "queryUserWithIdEmail", user);
    String sql = sqlInfo.getSql();
    Object[] params = sqlInfo.getParamsArr();
    System.out.println("-- 生成sql的为:\n" + sql);
    System.out.println("-- 生成sql的参数为:\n" + Arrays.toString(params));

    List<User> users = User.userDao.find(sql, params);
    renderJson(users);
}
```

打印的sql结果：

```sql
-- 生成sql的为:
select * from user where id = ?
-- 生成sql的参数为:
[3]
```

当把userId的值设为null时，打印的sql结果：

```sql
-- 生成sql的为:
select * from user where email LIKE ?
-- 生成sql的参数为:
[%san%]
```