# sinno-framework
sinno framework

很多时候，我们javaer只会使用第三方jar包，框架来进行code，做业务，很多底层实现都不了解。这是一件很悲哀的事。

so 我想和代码谈谈。故而临摹smart-framework进行了这个sinno-framework的开发。更底层的了解框架的原理。

目标：实现一套轻量级的javaWeb框架。

__github:[sinno-framework](https://github.com/clz619/sinno-framework)__

* 定义框架配置项  
* 加载配置项
* 类加载器  
* 实现bean容器
* 实现依赖注入功能
* 加载Controller
* 初始化框架
* 请求转发  
* 动态代理实现aop特性
* 使用aop实现事务控制