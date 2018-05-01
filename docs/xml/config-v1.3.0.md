对于很长的动态或统计性的SQL采用Java书写不仅冗长，且不易于调试和维护，因此更推荐你通过xml文件来书写sql，使得SQL和Java代码解耦，更易于维护和阅读。使用xml方式需要经过一些配置，使系统能读取到xml中的SQL信息到缓存中，使动态拼接更高效。

### 1.3.0的新配置方式

#### 1. 最简启动加载配置，只需指定需要扫描的XML文件位置即可

自1.3.0开始，沿用了默认大于配置的方式，为了能够识别到Zealot XML文件的命名空间和路径，可以不需要开发者再在ZealotConfig文件中的`configXml()`方法中配置了，可以直接指定XML文件所在项目的资源目录或相对路径即可，且XML文件的`zealots`根节点需要添加该文件区别其他zealot xml文件的命名空间(nameSpace)。可以直接在你项目的初始化启动类或者方法里面做如下配置即可：

```java
// 在你的启动类或方法中加入该语句，来初始化加载zealot xml目录中的xml命名空间和其对应的位置
// 如果参数 xmlLocations 值为空的话，则默认扫描你项目资源目录(及子目录)`zealot`下的所有Zealot XML SQL文件.
ZealotConfigManager.getInstance().initLoadXmlLocations("myzealots/xml");
```

> **注**：参数`xmlLocations`，表示zealot的XML文件所在的位置，多个用逗号隔开,可以是目录也可以是具体的xml文件.

如果采用这种扫描配置的方式，zealot中XML文件的`zealots`根节点中需要指定命名空间(nameSpace)属性，用来和其他zealot xml文件区分该来，同时方便zealot的调用，XML示例如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 查询老师相关的SQL信息，命名空间nameSpace为zealots的根节点属性，各xml文件的nameSpace不能相同，如果不填nameSpace则需在ZealotConfig中配置与xml的nameSpace映射值. -->
<zealots nameSpace="myTeacher">

    <!-- 根据Id查询学生信息. -->
    <zealot id="queryTeacherById">
        SELECT * FROM t_teacher AS t WHERE
        <equal field="t.c_id" value="id"/>
    </zealot>

</zealots>
```

```java
/**
 * 测试从扫描的zealot xml文件中生成sql.
 */
@Test
public void testQueryTeacherSql() {
    SqlInfo sqlInfo = Zealot.getSqlInfo("myTeacher", "queryTeacherById",
            ParamWrapper.newInstance("id", "123").toMap());
    String expectedSql = "SELECT * FROM t_teacher AS t WHERE t.c_id = ?";

    assertEquals(expectedSql, sqlInfo.getSql());
    assertArrayEquals(new Object[]{"123"}, sqlInfo.getParamsArr());
}
```

#### 2. 自定义标签注解扫描配置

当你的系统中需要用到自定义标签时，对你的Handler的配置也不需要再在ZealotConfig文件中的`configTagHandler()`方法中配置了，可以直接指定各Handler类所在项目的资源目录或相对路径即可。同时在Handler中使用`@Tagger`和`@Taggers`注解来标注该标签处理器所对应的标签、前缀、操作符等，配置和Handler使用示例如下：

```java
// 在你的启动类或方法中加入该语句，来初始化加载zealot handler目录中的注解和标签处理器
// 参数 handlerLocations 值不能为空，如果为空，也不会报错，不过不会产生任何作用而已.
ZealotConfigManager.getInstance()
        .initLoadXmlLocations("myzealots/xml, xml/zealots, abc/zealot-user.xml")
        .initLoadHandlerLocations("com.blinkfox.zealot.test.handler, com.blinkfox.myProject.zealot.Hello.java");
```

> **注**：参数`handlerLocations`，表示zealot的XML文件所在的位置，多个用逗号隔开,可以是目录也可以是具体的xml文件.

Handler和注解的示例如下：

```java
import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.annotation.Tagger;
import com.blinkfox.zealot.core.IConditHandler;

/**
 * 测试Tagger注解的Handler.
 *
 * @author blinkfox on 2018/4/28.
 */
@Tagger(value = "helloTagger", prefix = "Hello", symbol = "Tagger")
public class TaggerTestHandler implements IConditHandler {

    /**
     * 由于只是用来测试注解，所以这里只做简单的拼接.
     * @param source 构建所需的资源对象
     * @return sqlInfo
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        source.getSqlInfo().getJoin().append(source.getPrefix())
                .append(" ").append(source.getSuffix());
        return source.getSqlInfo();
    }

}
```

```java
import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.config.annotation.Tagger;
import com.blinkfox.zealot.config.annotation.Taggers;
import com.blinkfox.zealot.core.IConditHandler;

/**
 * 测试'Taggers'注解的Handler.
 * @see com.blinkfox.zealot.config.annotation.Taggers
 *
 * @author blinkfox on 2018/4/28.
 */
@Taggers({
        @Tagger(value = "hello", prefix = "hello", symbol = "blinkfox"),
        @Tagger(value = "hi", prefix = "hi", symbol = "blinkfox"),
        @Tagger(value = "hw", prefix = "hello", symbol = "world")
})
public class TaggersTestHandler implements IConditHandler {

    /**
     * 由于这个类只是用来测试注解，所以这里只做简单的字符串拼接.
     * @param source 构建所需的资源对象
     * @return sqlInfo
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        source.getSqlInfo().getJoin().append(source.getPrefix())
                .append(" ").append(source.getSuffix());
        return source.getSqlInfo();
    }

}
```

这样就可以在例的xml文件中使用自定义的标签了，xml中的使用示例如下：

```xml
<!-- 根据Id查询课程信息 -->
<zealot id="testTaggerHanderSql">
    <!-- helloTagger 的自定义标签在 com.blinkfox.zealot.test.handler.TaggerTestHandler 类中通过自定义注解来定义和实现的 -->
    <helloTagger />

    <!-- sayHello 等自定义标签在 com.blinkfox.zealot.test.handler.TaggersTestHandler 类中通过自定义注解来定义和实现的 -->
    <hello />
    <hi />
    <hw />
</zealot>
```

#### 3. 其他配置

Zealot的配置中，除了xml和Handler的配置外，还有一些其他普通配置项，主要有以下三种，在启动类或者方法中，通过Java的配置方式如下：

```java
NormalConfig.getInstance() // 获取普通配置的唯一实例
        .setDebug(false)  // 设置是否开启debug模式，这样每次调用都会实时从最新的xml文件中获取sql，默认值为false.
        .setPrintBanner(true) // 设置是否打印zealot的启动banner，默认为true.
        .setPrintSqlInfo(true); // 设置是否打印zealot的sql日志，默认为true.
```

#### 4. 新老版本兼用的配置方式

老版本的配置方式是采用Java配置的方式，需要新建一个`ZealotConfig`配置类（继承自`AbstractZealotConfig`），老版本通过Java启动类或方法加载配置的方式即为：

```java
// 直接指定类的class
ZealotConfigManager.getInstance().initLoad(MyZealotConfig.class);

// 或者类的class全名
ZealotConfigManager.getInstance().initLoad("com.blinkfox.config.MyZealotConfig");

// 或者类的实例,这个实例就可以直接从Spring等容器中获取了，不一定是new出来的
ZealotConfigManager.getInstance().initLoad(new MyZealotConfig());
```

新版本也完全兼容以前的版本，所以配置方式只需扩展两个参数即可，如下：

```java
ZealotConfigManager.getInstance().initLoad(MyZealotConfig.class, "zealot/xml", "com.blinkfox.zealot.test.handler");

// 或者
ZealotConfigManager.getInstance().initLoad("com.blinkfox.config.MyZealotConfig", "zealot/xml", "com.blinkfox.zealot.test.handler");

// 或者
ZealotConfigManager.getInstance().initLoad(new MyZealotConfig(), "zealot/xml", "com.blinkfox.zealot.test.handler");
```

当然，不填写后面两个的扫描位置，继续再`MyZealotConfig`类中指定配置xml路径和handler对应的标签、class也完全是可以的。