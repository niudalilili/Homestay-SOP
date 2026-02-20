package com.tanyde.config;


import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;

import com.tanyde.json.JacksonObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.List;

/**
 * 配置类，注册web层相关组件
 *
 * @param *
 * @author TanyDe
 * @return
 * @create 2026/1/4
 **/
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 指定校验规则：全局要求必须登录
                    StpUtil.checkLogin();
                }))
                .addPathPatterns("/**")
                // 放行不需要登录的接口
                .excludePathPatterns(
                        "/admin/employee/login",
                        "/user/user/login",
                        // 放行 Swagger/Knife4j 文档资源
                        "/doc.html",
                        "/doc.html/**",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs/swagger-config",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/favicon.ico"
                );
    }

    /**
     * 资源处理器：
     *
     * @param registry
     **/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    /**
     * 扩展Spring MVC框架的消息转化器
     **/
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        converter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到converters中
        converters.add(converter);
    }


}
