package com.lhamster;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/8
 */
@EnableDubbo
@SpringBootApplication
public class OrganizationsWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrganizationsWebApplication.class, args);
    }
}
