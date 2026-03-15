package com.tanyde.config;


import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    //注册SaToken拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 路由拦截器,自定义认证规则
        registry.addInterceptor(new SaInterceptor(handle -> {
                    //登录校验--拦截所有路
                    //SaRouter.match("/admin", r -> StpUtil.checkLogin());
                    //角色检验--拦截admin下的路由，必须具备admin 角色或者 super-admin 角色才可以通过认证
                    //SaRouter.match("/admin/**",r->StpUtil.checkRoleOr("admin","super-admin"));
                }))
                .addPathPatterns("/**")
                // 放行不需要登录的接口
                .excludePathPatterns(
                        "/admin/employee/login",
                        "/auth/login",
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


}
