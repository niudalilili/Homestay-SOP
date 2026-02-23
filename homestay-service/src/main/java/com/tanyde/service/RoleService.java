package com.tanyde.service;

import com.tanyde.dto.LoginDTO.RoleDTO;
import com.tanyde.dto.LoginDTO.RolePageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.vo.RoleVO;

public interface RoleService {
    //新增
    void add(RoleVO roleVO);
    //修改
    void update(RoleVO roleVO);
    //删除
    void delete(Long id);
    //根据id查询
    RoleDTO getById(Long id);
    //分页查询
    PageResult pageQuery(RolePageQueryDTO rolePQDTO);
}
