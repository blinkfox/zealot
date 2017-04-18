由于Zealot中SQL片段生成的标签大多是工具库预设或自定义的，但是在实现更为灵活的逻辑控制时就显得力不从心了，如果都通过自定义标签去实现更灵活多变的逻辑会显得很麻烦。因此，决定在1.0.6的版本中增加更为强大灵活的流程逻辑控制标签。自由的流程逻辑控制方式就意味着难以实现绑定变量参数的方式来生成SQL，而是即时生成替换变量值后的SQL。

### 使用示例

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

### 常用标签

Zealot的流程控制标签使用的是MVEL模板标签，所以，支持所有MVEL2.0的模板标签，这也正体现了Zealot动态SQL的强大特性。关于MVEL2.x的模板更详细的介绍请参考[这里][5]。

#### @{} 表达式

``@{}`表达式是最基本的形式。它包含一个对字符串求值的值表达式，并附加到输出模板中。例如：

```java
Hello, my name is @{person.name}
```

#### @code{} 静默代码标签

静默代码标记允许您在模板中执行MVEL表达式代码。它不返回值，并且不以任何方式影响模板的格式。

```java
@code{age = 23; name = 'John Doe'}
@{name} is @{age} years old
```
该模板将计算出：John Doe is 23 years old。

#### @if{}@else{} 控制流标签

``@if{}`和`@else{}`标签在MVEL模板中提供了完全的`if-then-else`功能。 例如：

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

#### @foreach{} Foreach迭代

`foreach`标签允许您在模板中迭代集合或数组。

!> 注意：foreach的语法已经在MVEL模板2.0中改变，以使用foreach符号来标记MVEL语言本身的符号。

```java
@foreach{item : products} 
 - @{item.serialNumber}
@end{}
```

  [5]: http://blinkfox.com/mvel-2-xmo-ban-zhi-nan/