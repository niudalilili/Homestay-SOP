package com.tanyde.service.Impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.tanyde.entity.LoginPO.Employee;
import com.tanyde.service.EmployeeService;
import com.tanyde.service.PermissionService;
import com.tanyde.service.RoleService;
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
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;

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
        //尝试从Session中获取缓存的权限列表
        SaSession session = StpUtil.getSessionByLoginId(loginId);
        if(session != null && session.has("permissions")){
            List< String>cachedPermissions = (List<String>) session.get("permissions");
            log.info("从Session中获取权限列表: {}", cachedPermissions);
            return cachedPermissions;
        }
        //Session中没有缓存，调用Service查询并缓存
        List<String> codes=permissionService.getCodesByIds(Long.valueOf(loginId.toString()));
        session.set("permissions", codes);
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
        //TODO:此处可以使用缓存避免每次鉴权都要查库
        //调用Service查询员工角色Code
        List<String> roles=new ArrayList<>();
        roles.add(roleService.getRoleCodeByEmployeeId(employeeId));

        return roles;
    }
}
