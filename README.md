# forest

一个业务无关的通用脚手架、工具框架

* forest-parent。父依赖模块。
* forest-common。公共模块，提供一些常用工具和基础类。
* forest-dependence。常用外部依赖版本管理。
* forest-mvc。MVC 相关扩展。
* forest-mybatis-plus-starter。mybatis-plus 相关扩展。
* forest-query。查询基础模块，其它类型数据库的查询操作可以基于此扩展。
* forest-framework。核心框架，提供通用日志打印功能、上下文工具。
* forest-rpc。一个简单的 Rpc 框架。

# forest-common

包括基础工具类。

* BeanUtils。Bean 拷贝工具类，基于 BeanCopier 实现，效率比 apache 和 spring 的 BeanUtils 要快很多。
* DateUtils。时间日期转换工具类。
* IdGenerator。Id 生成器。
* JsonUtils。Json 序列化和反序列化工具类。
* NetworkUtils。网络工具类。
* NumberUtils。数字工具类。
* OsUtils。操作系统工具类。
* RegexUtils。正则工具类。

# forest-mybatis-plus-starter

Mybatis-plus 扩展。

* Mybatis-plus 基于注解模式的查询。
* 流式查询方法扩展，大数据量情况下可极大提升查询效率。见`ForestSqlInjector`。

# forest-mvc

spring boot starter web 相关扩展功能。

* xss 攻击拦截。

```yaml
forest:
  mvc:
    # 是否启用 xss 拦截
    enable-xss: true
```

# forest-framework

框架核心层。提供如下功能：

* 日志打印。基于Spring AOP 实现，可定制打印参数和拦截路径，理论上只要是被 Spring 管理的 Bean 都可以拦截，只要在配置文件中全局配置或者在指定类或方法上加上`@LogPrinter` 注解即可。

```yaml
forest:
  log:
    ignore-text: "#该字段不打印#"
    ignore-req: ""
    ignore-resp: content
    package-path: online.qiqiang.forest.example.mvc.controller
    enable: true
```

* 上下文管理。提供上下文参数获取方法，支持远程上下文，推荐 redis 实现方式，需引入`spring-boot-starter-data-redis`依赖

```yaml
forest:
  context:
    type: redis
```

# forest-rpc

一个简单的 rpc 调用框架。
