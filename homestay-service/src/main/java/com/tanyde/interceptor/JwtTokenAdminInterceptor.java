package com.tanyde.interceptor;


import com.tanyde.constant.JwtClaimsConstant;
import com.tanyde.context.BaseContext;
import com.tanyde.properties.JwtProperties;
import com.tanyde.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *JWT令牌拦截器
 **/

@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
/**
 * 预处理请求的拦截器方法
 * 用于验证JWT令牌的有效性，确保只有合法请求能够访问Controller方法
 *
 * @param request HttpServletRequest对象，包含客户端请求信息
 * @param response HttpServletResponse对象，用于向客户端返回响应
 * @param handler 请求处理器的对象，可能是Controller方法或其他资源
 * @return boolean 返回true表示放行，返回false表示拦截请求
 * @throws Exception 可能抛出的异常
 */
    public boolean preHandle(HttpServletRequest request,
                               HttpServletResponse response,
                               Object handler) throws Exception {
        //判断当前拦截到的是Controller的方式还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //若拦截到的不是动态方法，直接放行
            return true;
        }

        //1.从请求头中获取令牌
        String token =request.getHeader(jwtProperties.getAdminTokenName());

        //2.校验令牌
        try {
            log.info("jwt校验:{}",token);
            Claims claims= JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(),token);
            Long empId=Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前员工id:{}",empId);
            BaseContext.setCurrentId(empId);
            //3.通过，放行
            return true;
        }catch (Exception ex) {
            //4.不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}