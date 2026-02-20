package com.tanyde.service.Impl;

import cn.dev33.satoken.stp.StpInterface;
import com.tanyde.entity.Employee;
import com.tanyde.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 **/
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 返回一个账号拥有权限码集合
     *
     * @param loginId
     * @param loginType
     * @return list
     **/
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //此处用于细粒度权限控制，暂时只使用角色控制
        //后续需要可在此处扩展
        return new ArrayList<>();
    }

    /**
     * 返回一个账号拥有的角色标识集合
     *
     * @param loginId
     * @param loginType
     * @return
     * @date:
     **/
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        log.info("开始获取用户角色，loginId: {}, loginType: {}", loginId, loginType);
        Long userId=Long.valueOf(loginId.toString());
        //调用Service查询用户信息
        //TODO:此处可以使用缓存避免每次鉴权都要查库
        Employee employee=employeeService.getById(userId);
        log.info("查询到的用户信息: {}", employee);
        
        //无此用户或者未配置权限则抛出异常
        if(employee ==null ){
            log.error("用户不存在，loginId: {}", loginId);
            throw new RuntimeException("用户不存在");
        }
        //如果是admin账号，直接返回admin角色
        if("admin".equals(employee.getUsername())){
            log.info("当前用户为admin，直接返回admin角色");
            return List.of("admin");
        }
        if(employee.getRole()==null){
            log.error("用户未配置权限，loginId: {}", loginId);
            throw new RuntimeException("用户未配置权限");
        }
        log.info("用户角色列表: {}", employee.getRole());
        return List.of(employee.getRole());
    }
}
