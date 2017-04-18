Zealot的核心功能就在于它XML格式的SQL配置文件。配置文件也仅仅是一个普通的XML文件，在XML中只需要少许的配置就可以动态生成自己所需要的查询条件。在XML中`zealots`标签作为根标签，其中的`zealot`则是一个独立SQL的元素标签，在`zealot`标签中才包含`like`、`andLike`、`andBetween`、`andIn`、`text`等条件标签,以下重点介绍各条件标签。

Zealot中默认自带了以下4类条件标签分别是：`equal`、`like`、`between`、`in`，分别对应着SQL查询中的等值匹配条件、模糊匹配条件、区间匹配条件以及范围匹配条件；四类条件标签又各自额外附带了两个连接前缀，分别是：`and`和`or`，用于表示逻辑`与`和`或`的情形，这两者更为常用，目前还未加入`非`的情形。还有一个灵活的`text`标签用来处理更通用灵活的场景。所以，zealot中总共带有13个“动态”标签，各标签的属性和生成SQL的说明如下。

### equal

#### 标签

```xml
<equal match="" field="" value=""/>
<andEqual match="" field="" value=""/>
<orEqual match="" field="" value=""/>
```

#### 属性介绍

- **match**，表示匹配条件。非必要（填）属性，如果不写（填）此属性，则视为必然生成此条件SQL片段；否则匹配结果为true时才生成，匹配结果为false时，不生成。匹配规则使用`MVEL2`表达式，关于[MVEL的语法文档][4]参考这里。
- **field**，表示对应数据库的字段，可以是数据库的表达式、函数等。必要（填）属性。
- **value**，表示参数值，对应Java中的名称。必要（填）属性，值也是使用`MVEL2`做解析。

#### 生成示例

```markup
标签:<equal field="nickname" value="nickName"></equal>
SQL片段的生成结果：nickname = ?
解释：必然生成此条SQL片段和参数

<andEqual match="?email != empty" field="email" value="email"></andEqual>
SQL片段的生成结果：AND email = ?
解释：如果email不等于空时，才生成此条SQL片段和参数
```

#### 与Equal类似的标签

- `moreThan` 大于
- `andMoreThan` 带and关键字的大于
- `orMoreThan` 带or关键字的大于
- `lessThan` 小于
- `andLessThan` 带and关键字的小于
- `orLessThan` 带or关键字的小于
- `moreEqual` 大于等于
- `andMoreEqual` 带and关键字的大于等于
- `orMoreEqual` 带or关键字的大于等于
- `lessEqual` 小于等于
- `andLessEqual` 带and关键字的小于等于
- `orLessEqual` 带or关键字的小于等于

### like

#### 标签

```xml
<like match="" field="" value=""/>
<andLike match="" field="" value=""/>
<orLike match="" field="" value=""/>
```

#### 属性介绍

- **match**，同上。
- **field**，同上。
- **value**，同上。

#### 生成示例

```markup
<andLike match="?email != empty" field="email" value="email"></andLike>

SQL片段的生成结果：AND email LIKE ?

解释：如果email不等于空时，才生成此条SQL片段和参数
```

### between

#### 标签

```xml
<between match="" field="" start="" end=""/>
<andBetween match="" field="" start="" end=""/>
<orBetween match="" field="" start="" end=""/>
```

#### 属性介绍

- **match**，同上。
- **field**，同上。
- **start**，表示区间匹配条件的开始参数值，对应Java中的名称，条件必填。
- **end**，表示区间匹配条件的结束参数值，对应Java中的名称，条件必填。

!> **注意**：Zealot中对start和end的空判断是检测是否是null,而不是空字符串，0等情况。所以，对start和end的空处理应该是null。

#### 生成示例

```markup
<andBetween match="?startAge != null || ?endAge != null" field="age" start="startAge" end="endAge"></andBetween>

start为null,end不为null，则SQL片段的生成结果：AND age >= ?
start不为null,end为null，则SQL片段的生成结果：AND age <= ?
start不为null,end不为null，则SQL片段的生成结果：AND age BETWEEN ? AND ?
start为null,end为null，则不生成SQL片段

**解释**：match标签是非必填的，区间查询中，靠start和end两种条件也可以组成一个简单的动态情形。如果start为空，end不为空，则是大于等于查询；如果start为空，end不为空，则是小于等于查询；如果start、end均不为空，则是区间查询；两者会均为空则不生产此条sql。
```

### in

#### 标签

```xml
<in match="" field="" value=""/>
<andIn match="" field="" value=""/>
<orIn match="" field="" value=""/>
```

#### 属性介绍

- **match**，同上。
- **field**，同上。
- **value**，表示参数的集合，值可以是数组，也可以是Collection集合，还可以是单个的值。必填

#### 使用生成示例

```markup
<andIn match="?sexs != empty" field="sex" value="sexs"></andIn>

SQL片段的生成结果：AND sex in (?, ?)

解释：如果sexs不等于空时，才生成此条SQL片段和参数(这里的sexs假设有两个值)
```

### text

text标签主要用于在标签内部自定义需要的文本和需要传递的各种参数，为SQL书写提供灵活性。

#### 标签

```xml
<text match="" value="">
    ...
</text>
```

#### 属性介绍

- **match**，同上。
- **value**，表示参数的集合，值可以是数组，也可以是Collection集合，还可以是单个的值。必填

#### 使用生成示例

```markup
<text match="" value="{name1, name2, email}">
    and name in (?, ?)
    and email = ?
</text>

SQL片段的生成结果：and name in (?, ?) and email = ?

解释：如果match为true、不填写或无match标签时，才生成此条SQL片段和自定义传递的参数，参数就是通过`name1`、`name2`和`email`组合成的数组或集合，或者直接传递集合或数组（此处组合而成的数组，如果是集合就把'{'换成'['即可）。
```

  [4]: http://mvel.documentnode.com/