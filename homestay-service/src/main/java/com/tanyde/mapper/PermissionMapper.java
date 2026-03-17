package com.tanyde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.dto.LoginDTO.PermissionPageQueryDTO;
import com.tanyde.entity.LoginPO.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    //分页查询权限
    IPage<Permission> pageQuery(Page<Permission> page,
                                @Param("dto") PermissionPageQueryDTO permissionPQDTO);

    //获得所有权限code
    List<String> listAll();

    //根据ids查询权限codes
    List<String> getCodesByIds(List<Long> ids);

    //根据员工id查询权限codes
    List<String> getCodesByEmployeeId(Long employeeId);

    //查询所有权限
    List<Permission> findAll();
}
