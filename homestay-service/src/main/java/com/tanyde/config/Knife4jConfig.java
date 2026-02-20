package com.tanyde.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Knife4jConfig {
    /**
     * 定义一个分组
     */
    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .packagesToScan("com.tanyde.controller")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("智慧民宿方案管理系统")
                        .description("基于Spring Boot 3 + Knife4j 的接口文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("TanyDe")
                                .email("609924214@qq.com"))
                );
    }

}
