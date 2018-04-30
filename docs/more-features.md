Zealot中除了上面介绍的一些功能之外，还有其他额外的辅助、简化开发的功能，以下作简要介绍。

### 上下文参数包装器

在`v1.2.0`版本中增加了上下文参数包装器`ParamWrapper`的功能，其本质上就是对`HashMap`的封装。在以前的版本中需要自己封装`JavaBean`对象或者Map对象来作为SQL拼接的上下文参数传入。

#### 前后对比的示例

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

#### ParamWrapper主要方法

- `newInstance()`，创建新的`ParamWrapper`实例。
- `newInstance(Map<String, Object> paramMap)`，传入已有的`Map`型对象，并创建新的`ParamWrapper`实例。
- `newInstance(String key, Object value)`，创建新的`ParamWrapper`实例，并创建一对key和value的键值对。
- `put(String key, Object value)`，向参数包装器中，`put`对应的key和value值。
- `toMap()`，返回填充了key、value后的Map对象。

### 表达式、模版解析器

在zealot中解析xml标签中的表达式或者模版是通过`Mvel`表达式语言来实现的，主要方法解析方法是封装在了`ParseHelper`的工具类中，通过该类让开发人员自己测试表达式也是极为方便的。以下作简要介绍。

#### 解析表达式

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

#### 解析模版

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

#### 真假判断

主要方法：

- isMatch(String match, Object paramObj)，是否匹配，常用于标签中的match值的解析，即如果match不填写，或者内容为空，或者解析出为正确的值，都视为`true`。
- isNotMatch(String match, Object paramObj)，是否不匹配，同`isMatch`相反，只有解析到的值是false时，才认为是false。
- isTrue(String exp, Object paramObj)，是否为true，只有当解析值确实为true时，才为true。

#### 消除`1 = 1`等无用SQL

在拼接动态SQL中避免不了会出现`1 = 1`等无用的子SQL片段，现在可以通过在生成完的SqlInfo对象中的`removeIfExist(subSql)`方法来消除它，其他类似的子SQL也都可以消除。使用示例如下：

```java
sqlInfo.removeIfExist(" 1 = 1 AND");

或者
sqlInfo.removeIfExist(" 1 <> 1");
```