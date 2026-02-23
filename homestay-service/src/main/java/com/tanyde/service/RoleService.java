package com.tanyde.service;

import com.tanyde.dto.LoginDTO.RoleDTO;
import com.tanyde.dto.LoginDTO.RolePageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.vo.RoleVO;

public interface RoleService {
    //新增
    void add(RoleDTO roleDTO);
    //修改
    void update(RoleDTO roleDTO);
    //删除
    void delete(Long id);
    //根据id查询
    RoleVO getById(Long id);
    //分页查询
    PageResult pageQuery(RolePageQueryDTO rolePQDTO);
}
