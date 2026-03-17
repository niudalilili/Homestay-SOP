package com.tanyde.service.Impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.tanyde.constant.RedisConstant;
import com.tanyde.entity.LoginPO.Employee;
import com.tanyde.service.EmployeeService;
import com.tanyde.service.PermissionService;
import com.tanyde.service.RedisService;
import com.tanyde.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 自定义权限验证接口扩展
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final RedisService redisService;

    /**
     * 返回一个账号拥有权限码集合
     *
     * @param loginId
     * @param loginType
     * @return list
     **/
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("开始获取用户权限，loginId: {}, loginType: {}", loginId, loginType);
        //尝试从Redis中获取缓存的权限列表
        String cacheKey = RedisConstant.EMPLOYEE_PERMISSION_PREFIX + loginId;
        Object cacheValue=redisService.get(cacheKey);
        if(cacheValue instanceof List){
            log.info("用户权限列表(从Redis中获取): {}",cacheValue);
            return (List<String>) cacheValue;
        }

        //Redis中没有缓存，调用Service从数据库查询并缓存
        List<String> codes=permissionService.getCodesByIds(Long.valueOf(loginId.toString()));
        redisService.set(cacheKey, codes,RedisConstant.EMPLOYEE_PERMISSION_TTL, TimeUnit.SECONDS);
        log.info("用户权限列表(从数据库中查询并缓存): {}", codes);
        return codes;
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
        Long employeeId=Long.valueOf(loginId.toString());
        //从Redis缓存中获取
        String cacheKey = RedisConstant.EMPLOYEE_ROLE_PREFIX + employeeId;
        Object cacheValue=redisService.get(cacheKey);
        if(cacheValue instanceof List){
            log.info("用户角色列表(从Redis中获取): {}",cacheValue);
            return (List<String>) cacheValue;
        }
        //Redis中没有缓存，调用Service从数据库查询并缓存
        //调用Service查询员工角色Code
        String roleCode=roleService.getRoleCodeByEmployeeId(employeeId);
        List<String> roles=new ArrayList<>();
        roles.add(roleCode);
        redisService.set(cacheKey, roles,RedisConstant.EMPLOYEE_ROLE_TTL, TimeUnit.SECONDS);
        return roles;
    }
}
