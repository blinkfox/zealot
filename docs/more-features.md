Zealot中除了上面介绍的一些功能之外，还有其他额外的辅助、简化开发的功能，以下作简要介绍。

### 上下文参数包装器

在`v1.2.0`版本中增加了上下文参数包装器`ParamWrapper`的功能，其本质上就是对`HashMap`的封装。在以前的版本中需要自己封装`JavaBean`对象或者Map对象来作为SQL拼接的上下文参数传入：

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