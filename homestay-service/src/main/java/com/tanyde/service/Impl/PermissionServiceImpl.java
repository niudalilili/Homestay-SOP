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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

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
}
