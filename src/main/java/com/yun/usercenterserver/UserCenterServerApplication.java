package com.yun.usercenterserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* 注  解
*     通过在类、方法、字段上添加特定注解，向 Spring 框架传递配置规则、行为指令或依赖注入等逻辑，
*  实现自动配置、组件扫描、功能启用、切面编程等核心能力，替代传统 XML 配置，让 Spring 应用开发更简洁高效。
* */

// 它相当于同时使用了@Configuration、@EnableAutoConfiguration和@ComponentScan这三个注解，是 SpringBoot 项目的核心注解
@SpringBootApplication
// 指定要扫描的 Mapper 接口所在的包，让 Spring 能够自动发现并注册这些 Mapper 接口， 使其能够被 Spring 容器管理
@MapperScan("com.yun.usercenterserver.mapper")
public class UserCenterServerApplication {
    // 启动 springboot 项目
    public static void main(String[] args) {
        SpringApplication.run(UserCenterServerApplication.class, args);
    }

}
