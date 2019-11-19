package com.andy.api;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 18:32
 */
@EnableTransactionManagement
@SpringBootApplication(
        exclude = DruidDataSourceAutoConfigure.class
)
@ComponentScan(
        basePackages = {
                "com.andy.api",
                "com.andy.mybatis",
                "com.andy.core"
        }
)
@EnableSwagger2Doc
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class);
    }
}
