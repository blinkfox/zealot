### 无参静态方法

!> **注意**：这里的**无参静态方法**是指拼接SQL时仅仅拼接文本字符串，不会拼接SQL的有序参数，且无动态判断是否生成该段SQL片段的能。作用是和待拼接的字符串自动拼接在一起，省去了`SQL关键字`的书写，目的是用来提高SQL的可读性。

SQL中的关键字很多，`ZealotKhala`封装了大多数常用的关键字作为连接SQL字符串的方法，如上面总体示例所列出的`select()`、`from()`、`select()`等，在流式拼接的过程中，使得SQL的可读性大大提高了。下面列出了大多数常用的关键字方法，来**用于拼接字符串文本，但不能传递SQL参数**。

- insertInto(String text)
- values(String text)
- deleteFrom(String text)
- update(String text)
- select(String text)
- from(String text)
- and(String text)
- or(String text)
- as(String text)
- set(String text)
- innerJoin(String text)
- leftJoin(String text)
- rightJoin(String text)
- fullJoin(String text)
- on(String text)
- orderBy(String text)
- groupBy(String text)
- having(String text)
- limit(String text)
- offset(String text)
- asc()
- desc()
- union()
- unionAll()

> 以上方法主要作用是：用方法名所代表的关键字后追加空格，再拼接上`text`文本参数，其方法名称已经体现了具体用途和使用场景，这里不在赘述。

### 有参动态(或静态)方法

> text(String text, Object... values) 

拼接`text`字符串，并对`text`的SQL字符串中绑定变量追加有序参数，values是任意类型的不定参数，即可以传递数组或任意个数的变量，用来作为该SQL片段的有序参数。使用示例如下：

```java
SqlInfo sqlInfo = ZealotKhala.start()
        ...
        .text("AND u.email = ? AND u.age >= ?", "san@163.com", 21)
```

> text(boolean match, String text, Object... values)

该`text()`方法同前面的方法类似，其多出的第一个boolean型参数，用来表示是否生成该SQL片段及有序参数。即如果`match`的结果为`true`时，就同上一个`text()`方法，生成此SQL片段及参数，否则不生成。

> param(Object... values)
> param(Collection<?> values)

只有一个纯粹的作用，那就是在当前拼接SQL的上下文中追加有序参数，值可以是任意类型不定个数的变量或常量，也可以是数组或Java的集合。

### equal

#### 方法介绍

equal系列是用来拼接SQL中等值查询的系列方法，生成如：` u.email = ? `这样的等值查询且附带绑定参数的功能，其主要包含如下方法：

- equal(String field, Object value)
- equal(String field, Object value, boolean match)
- andEqual(String field, Object value)
- andEqual(String field, Object value, boolean match)
- orEqual(String field, Object value)
- orEqual(String field, Object value, boolean match)
- notEqual(String field, Object value)
- notEqual(String field, Object value, boolean match)
- andNotEqual(String field, Object value)
- andNotEqual(String field, Object value, boolean match)
- orNotEqual(String field, Object value)
- orNotEqual(String field, Object value, boolean match)

**方法解释**：

- equal、andEqual、orEqual，分别表示拼接等值查询SQL的前缀为`空字符串`,` AND `，` OR `。
- field，表示数据库字段
- value，表示Java中的变量或常量值
- match，表示是否生成该SQL片段，值为`true`时生成，否则不生成

#### 使用示例

```java
/**
 * 初始化.
 */
@BeforeClass
public static void init() {
    context = new HashMap<String, Object>();
    context.put("id", "3");
    context.put("name", "zhagnsan");
    context.put("myEmail", "zhagnsan@163.com");
    context.put("myAge", 25);
}

/**
 * equal相关方法测试.
 */
@Test
public void testEqual() {
    SqlInfo sqlInfo = ZealotKhala.start()
            .equal("u.id", context.get("id"), "4".equals(context.get("id")))
            .equal("u.nick_name", context.get("name"))
            .andEqual("u.true_age", context.get("myAge"))
            .andEqual("u.true_age", context.get("myAge"), context.get("myAge") != null)
            .andEqual("u.email", context.get("myAge"), context.get("myEmail") == null)
            .orEqual("u.email", context.get("myEmail"))
            .end();
    String sql = sqlInfo.getSql();
    Object[] arr = sqlInfo.getParamsArr();

    log.info("-- testEqual()方法生成的sql信息:\n" + sql + "\n-- 参数为:\n" + Arrays.toString(arr));
}
```

打印生成的SQL片段信息如下：

```sql
-- testEqual()方法生成的sql信息:
u.nick_name = ? AND u.true_age = ? AND u.true_age = ? OR u.email = ?
-- 参数为:
[zhagnsan, 25, 25, zhagnsan@163.com]
```

### 同equal类似的

同equal（等于）类似的系列还有不等于、大于、小于、大于等于、小于等于、模糊查询，各系列分别如下：

- `notEqual` 不等于
- `moreThan` 大于
- `lessThan` 小于
- `moreEqual` 大于等于
- `lessEqual` 小于等于
- `like` 模糊查询
- `likePattern` 根据自定义模式来匹配

!> 以上各系列的方法也同equal，这里就不再赘述了。

### between

#### 方法介绍

between系列是用来拼接SQL中区间查询的系列方法，生成如：` u.age BETWEEN ? AND ? `这样的区间查询功能，主要包含如下方法：

- between(String field, Object startValue, Object endValue)
- between(String field, Object startValue, Object endValue, boolean match)
- andBetween(String field, Object startValue, Object endValue)
- andBetween(String field, Object startValue, Object endValue, boolean match)
- orBetween(String field, Object startValue, Object endValue)
- orBetween(String field, Object startValue, Object endValue, boolean match)

**方法解释**：

- between、andBetween、orBetween，分别表示拼接区间查询SQL的前缀为`空字符串`,` AND `，` OR `
- field，表示数据库字段
- startValue，表示区间查询的开始值
- endValue，表示区间查询的结束值
- match，表示是否生成该SQL片段，值为`true`时生成，否则不生成

#### 使用示例

```java
/**
 * 初始化.
 */
@BeforeClass
public static void init() {
    context = new HashMap<String, Object>();
    context.put("startAge", 18);
    context.put("endAge", 26);
    context.put("startBirthday", null);
    context.put("endBirthday", "2010-05-28");
}

/**
 * between相关方法测试.
 */
@Test
public void testBetween() {
    SqlInfo sqlInfo = ZealotKhala.start()
            .between("u.age", startAge, endAge)
            .andBetween("u.age", startAge, endAge, startAge != null && endAge != null)
            .orBetween("u.age", startAge, endAge, startAge != null && endAge != null)
            .end();
    String sql = sqlInfo.getSql();
    Object[] arr = sqlInfo.getParamsArr();

    log.info("-- testEqual()方法生成的sql信息:\n" + sql + "\n-- 参数为:\n" + Arrays.toString(arr));
}
```

打印的SQL如下：

```sql
-- testBetween()方法生成的sql信息:
u.age BETWEEN ? AND ? AND u.age BETWEEN ? AND ? AND u.birthday <= ?
-- 参数为:
[18, 26, 18, 26, 2010-05-28]
```

!> **注意**：Zealot中会对start和end的值做null的空检测。区间查询中如果start为空，end不为空，则是大于等于查询；如果start为空，end不为空，则是小于等于查询；如果start、end均不为空，则是区间查询；两者会均为空则不生产此条sql。

### in

#### 方法介绍

in系列是用来拼接SQL中范围查询的系列方法，生成如：` u.sex in (?, ?) `这样的范围查询功能，主要包含如下方法：

- in(String field, Object[] values)
- in(String field, Object[] values, boolean match)
- andIn(String field, Object[] values)
- andIn(String field, Object[] values, boolean match)
- orIn(String field, Object[] values)
- orIn(String field, Object[] values, boolean match)
- notIn(String field, Object[] values)
- notIn(String field, Object[] values, boolean match)
- andNotIn(String field, Object[] values)
- andNotIn(String field, Object[] values, boolean match)
- orNotIn(String field, Object[] values)
- orNotIn(String field, Object[] values, boolean match)

**方法解释**：

- in、andIn、orIn，分别表示拼接范围查询SQL的前缀为`空字符串`,` AND `，` OR `。
- field，表示数据库字段
- values，表示范围查询需要的参数的数组
- match，表示是否生成该SQL片段，值为`true`时生成，否则不生成

#### 使用示例如下：

```java
/**
 * 初始化.
 */
@BeforeClass
public static void init() {
    context = new HashMap<String, Object>();
    context.put("sexs", new Integer[] {0, 1});
}

/**
 * in相关方法测试.
 */
@Test
public void testBetween() {
    Integer[] sexs = (Integer[]) context.get("sexs");

    SqlInfo sqlInfo = ZealotKhala.start()
            .in("u.sex", sexs)
            .andIn("u.sex", sexs, sexs != null)
            .orIn("u.sex", sexs)
            .end();
    String sql = sqlInfo.getSql();
    Object[] arr = sqlInfo.getParamsArr();

    log.info("-- testIn()方法生成的sql信息:\n" + sql + "\n-- 参数为:\n" + Arrays.toString(arr));
}
```

打印的SQL如下：

```sql
-- testIn()方法生成的sql信息:
u.sex in (?, ?) AND u.sex in (?, ?) OR u.sex in (?, ?)
-- 参数为:
[0, 1, 0, 1, 0, 1]
```

### isNull

#### 方法介绍

`isNull`系列是用来拼接SQL中判断字段为null值或不为null值情况的系列方法，生成如：` u.state IS NULL `这样SQL片段的功能，主要包含如下方法：

- isNull(String field)
- isNull(String field, boolean match)
- andIsNull(String field)
- andIsNull(String field, boolean match)
- orIsNull(String field)
- orIsNull(String field, boolean match)
- isNotNull(String field)
- isNotNull(String field, boolean match)
- andIsNotNull(String field)
- andIsNotNull(String field, boolean match)
- orIsNotNull(String field)
- orIsNotNull(String field, boolean match

**方法解释**：

- isNull、andIsNull、orIsNull，分别表示拼接null值判断SQL的前缀为`空字符串`,` AND `，` OR `。
- field，表示数据库字段
- match，表示是否生成该SQL片段，值为`true`时生成，否则不生成

#### 使用示例如下：

```java
/**
 * IS NULL相关方法测试.
 */
@Test
public void testIsNull() {
    long start = System.currentTimeMillis();

    SqlInfo sqlInfo = ZealotKhala.start()
            .isNull("a.name")
            .isNull("b.email")
            .isNull("a.name", true)
            .isNull("b.email", false)
            .andIsNull("a.name")
            .andIsNull("b.email")
            .andIsNull("a.name", false)
            .andIsNull("b.email", true)
            .orIsNull("a.name")
            .orIsNull("b.email")
            .orIsNull("a.name", false)
            .orIsNull("b.email", true)
            .end();

    log.info("testIn()方法执行耗时:" + (System.currentTimeMillis() - start) + " ms");
    String sql = sqlInfo.getSql();
    Object[] arr = sqlInfo.getParamsArr();

    // 断言并输出sql信息
    assertEquals("a.name IS NULL b.email IS NULL a.name IS NULL AND a.name IS NULL AND b.email IS NULL "
            + "AND b.email IS NULL OR a.name IS NULL OR b.email IS NULL OR b.email IS NULL", sql);
    assertArrayEquals(new Object[]{} ,arr);
    log.info("-- testIsNull()方法生成的sql信息:\n" + sql + "\n-- 参数为:\n" + Arrays.toString(arr));
}
```

打印的SQL如下：

```sql
-- testIsNull()方法生成的sql信息:
a.name IS NULL b.email IS NULL a.name IS NULL AND a.name IS NULL AND b.email IS NULL AND b.email IS NULL OR a.name IS NULL OR b.email IS NULL OR b.email IS NULL
-- 参数为:
[]
```

### doAnything

> doAnything(ICustomAction action)

> doAnything(boolean match, ICustomAction action)

这两个方法主要用来方便你在链式拼接的过程中，来完成更多自定义、灵活的操作。`match`意义和上面类似，值为true时才执行，`ICustomAction`是你自定义操作的函数式接口，执行时调用`execute()`方法,使用示例如下：

```java
SqlInfo sqlInfo = ZealotKhala.start()
        .doAnything(true, new ICustomAction() {
            @Override
            public void execute(final StringBuilder join, final List<Object> params) {
                join.append("abc111");
                params.add(5);
                log.info("执行了自定义操作，可任意拼接字符串和有序参数...");
            }
        })
        .end();
```

如果是Java8的话，可以将以上代码转成Lambda表达式，代码如下：

```java
SqlInfo sqlInfo = ZealotKhala.start()
        .doAnything(true, (join, params) -> {
                join.append("abc111");
                params.add(5);
                log.info("执行了自定义操作，可任意插入、拼接字符串和有序参数...");
            }
        })
        .end();
```