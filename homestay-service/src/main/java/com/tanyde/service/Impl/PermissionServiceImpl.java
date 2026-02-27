package com.tanyde.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tanyde.dto.LoginDTO.PermissionDTO;
import com.tanyde.dto.LoginDTO.PermissionPageQueryDTO;
import com.tanyde.dto.LoginDTO.RoleDTO;
import com.tanyde.entity.LoginPO.Permission;
import com.tanyde.entity.LoginPO.Role;
import com.tanyde.mapper.PermissionMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanyde.mapper.RoleMapper;
import com.tanyde.vo.PermissionGroupVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 根据ID获得权限信息
     *
     * @param id
     * @return com.tanyde.dto.LoginDTO.RoleDTO
     * @date 2026/2/22 16:31
     **/
    @Override
    public PermissionDTO getById(Long id) {
        PermissionDTO permissionDTO = new PermissionDTO();
        //获取role数据
        Permission permission = permissionMapper.getById(id);
        BeanUtils.copyProperties(permission, permissionDTO);
        return permissionDTO;
    }

    /**
     * 根据EmployeeId获得权限codes
     *
     * @param employeeId
     * @return java.util.List<java.lang.String>
     * @date 2026/2/22 16:31
     **/
    @Override
    public List<String> getCodesByIds(Long employeeId) {
        // 1. 获取用户角色
        String roleCode = roleMapper.getRoleCodeByEmployeeId(employeeId);
        
        // 2. 如果是超级管理员，返回所有权限
        if ("super_admin".equals(roleCode)) {
            return permissionMapper.listAll();
        }
        
        // 3. 否则查询该用户拥有的权限
        List<String> codes = permissionMapper.getCodesByEmployeeId(employeeId);
        return codes;
    }

    /**
     * 分页查询权限信息
     *
     * @param permissionPQDTO
     * @return com.tanyde.result.PageResult
     * @date 2026/2/22 16:32
     **/
    @Override
    public PageResult pageQuery(PermissionPageQueryDTO permissionPQDTO) {
        //分页查询
        PageHelper.startPage(permissionPQDTO.getPage(), permissionPQDTO.getPageSize());
        Page<Permission> page = permissionMapper.pageQuery(permissionPQDTO);

        long total = page.getTotal();
        //将PO转化为DTO的list
        List<PermissionDTO> records = page.getResult().stream()
                .map(permission -> {
                    PermissionDTO permissionDTO = new PermissionDTO();
                    BeanUtils.copyProperties(permission, permissionDTO);
                    return permissionDTO;
                }).collect(Collectors.toList());

        return new PageResult(total, records);
    }

    /**
     * 获得所有权限code
     *
     * @return java.util.List<com.tanyde.dto.LoginDTO.RoleDTO>
     * @date 2026/2/22 16:32
     **/
    @Override
    public List<String> listAll() {
        return permissionMapper.listAll();
    }

    /**
     * 获取权限树
     *
     * @return java.util.List<com.tanyde.vo.PermissionGroupVO>
     * @date 2026/2/24
     **/
    @Override
    public List<PermissionGroupVO> getTree() {
        // 1. 获取所有权限
        List<Permission> allPermissions = permissionMapper.findAll();
        
        // 2. 按模块分组 (这里简单通过code的前缀来分组，例如 activity:add -> activity)
        Map<String, List<Permission>> groupedPermissions = allPermissions.stream()
                .collect(Collectors.groupingBy(p -> {
                    String code = p.getCode();
                    if (code != null && code.contains(":")) {
                        return code.split(":")[0];
                    }
                    return "other";
                }));

        // 3. 构建返回结果
        List<PermissionGroupVO> result = new ArrayList<>();
        // 定义模块名称映射（可选）
        Map<String, String> moduleNames = Map.of(
                "activity", "活动管理",
                "employee", "员工管理",
                "system", "系统管理",
                "role", "角色管理"
        );

        groupedPermissions.forEach((key, list) -> {
            String moduleName = moduleNames.getOrDefault(key, key);
            // 如果key是system:role这种形式的，可能需要特殊处理，这里暂且简化
            if ("other".equals(key)) moduleName = "其他";
            
            result.add(PermissionGroupVO.builder()
                    .module(moduleName)
                    .permissions(list)
                    .build());
        });

        return result;
    }
}
