package com.tanyde.mapper;

import com.github.pagehelper.Page;
import com.tanyde.dto.LoginDTO.PermissionPageQueryDTO;
import com.tanyde.entity.LoginPO.Permission;
import com.tanyde.entity.LoginPO.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {

    //根据id查询权限
    Permission getById(Long id) ;

    //分页查询权限
    Page<Permission> pageQuery(PermissionPageQueryDTO permissionPQDTO);

    //获得所有权限code
    List<String> listAll();
}
