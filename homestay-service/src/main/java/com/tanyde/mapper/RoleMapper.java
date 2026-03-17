package com.tanyde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.dto.LoginDTO.RolePageQueryDTO;
import com.tanyde.entity.LoginPO.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {


    /**
     * 添加角色权限关联表
     *
     * @param id
     * @param permissionIds
     * @return void
     * @date 2026/2/23 20:58
     **/
    void addRolePermission(@Param("id") Long id,
                           @Param("permissionIds")List<Long> permissionIds);

    /**
     * 删除角色权限关联表
     *
     * @param roleId
     * @return void
     * @date 2026/2/23 20:59
     **/
    void deleteRolePermission(Long roleId);

    /**
     * 根据roleId查询权限
     *
     * @param roleId
     * @return java.utils.List<java.lang.Long>
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
    IPage<Role> pageQuery(Page<Role> page,
                          @Param("dto") RolePageQueryDTO rolePQDTO);

    /**
     * 根据角色编码查询角色数量
     *
     * @param code
     * @return java.lang.Integer
     * @date 2026/2/23 22:24
     **/
    Integer countByCode(String code);

    /**
     * 根据员工id查询角色
     *
     * @param employeeId
     * @return java.lang.String
     * @date 2026/2/23 22:24
     **/
    String getRoleCodeByEmployeeId(Long employeeId);
}
