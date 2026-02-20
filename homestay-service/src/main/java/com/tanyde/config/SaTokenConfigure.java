package com.tanyde.config;


import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    //注册SaToken拦截器，打开注解鉴权功能
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册 Sa-Token 拦截器，打开注解鉴权功能
//        registry.addInterceptor(new SaInterceptor(handle -> {
//            // 1. 指定校验规则：全局要求必须登录
//                    StpUtil.checkLogin();
//                }))
//                // 2. 拦截所有路径
//                .addPathPatterns("/**")
//                // 3. 放行不需要登录的接口
//                // 管理端登录
//                .excludePathPatterns("/admin/employee/login")
//                // 用户端登录
//                .excludePathPatterns("/user/user/login")
//                // 放行 Swagger/Knife4j 文档资源
//                .excludePathPatterns(
//                        "/doc.html",
//                        "/webjars/**",
//                        "/swagger-resources/**",
//                        "/v2/api-docs/**",
//                        "/favicon.ico"
//                );
//    }



}
