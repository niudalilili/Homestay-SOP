package com.tanyde.mapper;

import com.github.pagehelper.Page;
import com.tanyde.annotation.AutoFill;
import com.tanyde.dto.LoginDTO.RolePageQueryDTO;
import com.tanyde.entity.LoginPO.Role;
import com.tanyde.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface RoleMapper {


    /**
     * 添加角色
     *
     * @param role
     * @return void
     * @date 2026/2/23 20:05
     **/
    @AutoFill(value = OperationType.INSERT)
    void add(Role role);

    /**
     * 添加角色权限关联表
     *
     * @param id
     * @param permissionIds
     * @return void
     * @date 2026/2/23 20:58
     **/
    @AutoFill(value = OperationType.INSERT)
    void addRolePermission(Long id, List<Long> permissionIds);

    /**
     * 修改角色
     *
     * @param role
     * @return void
     * @date 2026/2/23 20:06
     **/
    @AutoFill(value = OperationType.UPDATE)
    void update(Role role);

    /**
     * 删除角色权限关联表
     *
     * @param roleId
     * @return void
     * @date 2026/2/23 20:59
     **/
    void deleteRolePermission(Long roleId);

    /**
     * 删除角色
     *
     * @param id
     * @return void
     * @date 2026/2/23 20:59
     **/
    void deletRole(Long id);

    /**
     * 根据id查询角色
     *
     * @param roleId
     * @return com.tanyde.entity.LoginPO.Role
     * @date 2026/2/23 20:59
     **/
    Role getById(Long roleId);

    /**
     * 根据roleId查询权限
     *
     * @param roleId
     * @return java.util.List<java.lang.Long>
     * @date 2026/2/23 22:19
     **/
    List<Long> getPermissionIdsByRoleId(Long roleId);

    /**
     * 分页查询
     *
     * @param rolePQDTO
     * @return com.github.pagehelper.Page<com.tanyde.entity.LoginPO.Role>
     * @date 2026/2/23 22:24
     **/
    Page<Role> pageQuery(RolePageQueryDTO rolePQDTO);
}
