package com.tanyde.config;


import com.tanyde.properties.AliOssProperties;
import com.tanyde.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aloOssUtil(AliOssProperties properties) {
        log.info("创建阿里云OSS工具类对象");
        return new AliOssUtil(
                properties.getEndpoint(),
                properties.getAccessKeyId(),
                properties.getAccessKeySecret(),
                properties.getBucketName()
        );
    }
}
