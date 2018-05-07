## 支持场景

适用于`Java (web)`项目，`JDK1.6`及以上

## 安装集成

以下仅列举`Maven`和`Gradle`的使用方式，你也可以直接将此jar包加入到你的`classpath`中。

### Maven

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>zealot</artifactId>
    <version>1.3.1</version>
</dependency>
```

### Gradle

```bash
compile 'com.blinkfox:zealot:1.3.1'
```

### SpringBoot

如果你的项目是SpringBoot2.x，那么可以直接引入Zealot的[Starter](https://github.com/blinkfox/zealot-spring-boot-starter)，且几乎不需要做任何其他方面的配置即可使用，这里是[zealot-spring-boot-starter](https://github.com/blinkfox/zealot-spring-boot-starter)的引入及使用方式说明。

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>zealot-spring-boot-starter</artifactId>
</dependency>
```