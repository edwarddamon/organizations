package com.lhamster;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/8
 */
@EnableDubbo
@SpringBootApplication
@MapperScan("com.lhamster.mapper")
public class OrganizationsServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrganizationsServerApplication.class, args);
    }
}
