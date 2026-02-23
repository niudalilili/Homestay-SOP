package com.tanyde.service;

import com.tanyde.dto.LoginDTO.PermissionDTO;
import com.tanyde.dto.LoginDTO.PermissionPageQueryDTO;
import com.tanyde.result.PageResult;

import java.util.List;


public interface PermissionService {

    /**
     * 根据id获取权限信息
     *
     * @param id
     * @return com.tanyde.dto.LoginDTO.RoleDTO
     * @date 2026/2/22 16:25
     **/
    PermissionDTO getById(Long id);

    /**
     * 分页查询
     *
     * @param permissionPageQueryDTO
     * @return com.tanyde.result.PageResult
     * @date 2026/2/22 16:29
     **/
    PageResult pageQuery(PermissionPageQueryDTO permissionPageQueryDTO);

    /**
     * 获取所有权限信息
     *
     * @return java.util.List<com.tanyde.dto.LoginDTO.RoleDTO>
     * @date 2026/2/22 16:29
     **/
    List<String> listAll();

}
