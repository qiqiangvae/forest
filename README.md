# forest
一个业务无关的通用脚手架、工具框架

* forest-parent。父依赖模块。
* forest-common。公共模块。
* forest-dependence。外部依赖版本管理。
* forest-mvc。MVC 相关扩展。
* forest-mybatis-plus-starter。mybatis-plus 相关扩展。
* forest-query。查询基础模块。

# forest-common

包括基础工具类。

* BeanUtils。Bean 拷贝工具类，基于 BeanCopier 实现，效率比apache和spring 的 BeanUtils 要快很多。
* DateUtils。时间日期转换工具类。
* IdGenerator。Id 生成器。
* JsonUtils。Json 序列化和反序列化工具类。
* NetworkUtils。网络工具类。
* NumberUtils。数字工具类。
* OSUtils。操作系统工具类。
* RegexUtils。正则工具类。

# forest-mybatis-plus-starter

Mybatis-plus 扩展。

* Mybatis-plus 基于注解模式的查询。
* 流式查询方法扩展。

# forest-mvc

spring boot starter web 相关扩展功能。

* xss 攻击拦截。

```yaml
forest:
  mvc:
    # 是否启用 xss 拦截
    enable-xss: true
```

