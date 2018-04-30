在Java中书写中等长度的SQL，用`+`连接的字符串或`StringBuilder`等尤其是动态字符串，会导致SQL的可读性很差，在`Zealot v1.0.4`版本中提供了一个额外高效的SQL字符串链式拼接工具`Khala`(**已被弃用**)，但`Khala`只提供拼接字符串的功能，并不具有返回动态SQL和参数的特性，便决定在v1.1.0版本中新增了`ZealotKhala`，`ZealotKhala`也采用了`流式API`的方式来书写“更流畅”的动态SQL，且会得到动态SQL的有序参数。

`ZealotKhala`使用方式如下：

```java
SqlInfo sqlInfo = ZealotKhala.start()
        ...
        .end();

String sql = sqlInfo.getSql();
Object[] arr = sqlInfo.getParamsArr();
```

> **解释**：调用静态`start()`方法开始初始化拼接SQL，最后调用`end()`方法得到最终拼接的`SqlInfo`实例；其中，可以通过`SqlInfo`的`getSql()`和`getParamsArr()`分别得到拼接的带绑定参数的字符串和有序参数。
