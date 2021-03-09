package com.lhamster.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/9
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(value = "swagger.enabled", havingValue = "true")
public class Swagger2Configuration {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) //下面自己配置的apiInfo
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lhamster.controller")) //想要使用呢swagger注解的包路径
                .paths(PathSelectors.any()) // 默认选项
                .build();
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder() // 这里注意如果是swagger1是new ApiInfo()
                .title("智慧社团在线文档") //当前行及下面两行可不写
                .version("1.0")
                .description("智慧社团在线文档，啷个哩个啷")
                .build();
    }
}
