# 基于spring boot与mybatis集成多数据源
### 多数据源配置，在我们集成多个系统或者对接的时候经常会用到，结合springboot、druid提供了比较方便的集成方案
### 大概的思路一下：
- yml中配置多个数据源信息
- 通过AOP切换不同数据源
- 配合mybatis plus使用
  <!--        spring-->
 spring-boot.version：2.1.5.RELEASE
 <!--        mybatis-->
 mybatis.version：2.0.1
 
 druid.version：1.1.16
 
 mybatis-plus.version：3.1.1
 
 
 
 
