package com.tanyde.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tanyde.dto.LoginDTO.RoleDTO;
import com.tanyde.dto.LoginDTO.RolePageQueryDTO;
import com.tanyde.entity.LoginPO.Role;
import com.tanyde.mapper.RoleMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.RoleService;
import com.tanyde.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 添加角色
     *
     * @param roleVO
     * @return void
     * @date 2026/2/23 20:29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(RoleVO roleVO) {
        // 获取权限id
        List<Long> permissionIds = roleVO.getPermissionIds();
        // 封装角色数据
        Role role=new Role();
        BeanUtils.copyProperties(roleVO,role);
        // 添加角色
        roleMapper.add(role);
        // 添加角色权限关系
        roleMapper.addRolePermission(role.getId(),permissionIds);
    }

    /**
     * 更新角色
     *
     * @param roleVO
     * @return void
     * @date 2026/2/23 20:29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleVO roleVO) {
        // 获取角色id和权限ids
        Long roleId = roleVO.getId();
        List<Long> permissionIds = roleVO.getPermissionIds();
        // 封装角色数据
        Role role=new Role();
        BeanUtils.copyProperties(roleVO,role);
        // 更新角色
        roleMapper.update(role);
        // 修改角色权限关系
        roleMapper.deleteRolePermission(roleId);
        roleMapper.addRolePermission(roleId,permissionIds);
    }

    /**
     * 删除角色
     *
     * @param id
     * @return void
     * @date 2026/2/23 20:30
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 删除角色
        roleMapper.deletRole(id);
        // 删除角色权限关系
        roleMapper.deleteRolePermission(id);
    }

    /**
     * 根据id查询
     *
     * @param roleId
     * @return com.tanyde.dto.LoginDTO.RoleDTO
     * @date 2026/2/23 20:30
     **/
    @Override
    public RoleDTO getById(Long roleId) {
        // 获取角色id和权限ids
        Role role = roleMapper.getById(roleId);
        List<Long> permissionIds = roleMapper.getPermissionIdsByRoleId(roleId);
        // 封装数据
        RoleDTO roleDTO = new RoleDTO() ;
        BeanUtils.copyProperties(role,roleDTO);
        roleDTO.setPermissionIds(permissionIds);

        return roleDTO;
    }

    /**
     * 分页查询
     *
     * @param rolePQDTO
     * @return com.tanyde.result.PageResult
     * @date 2026/2/23 20:32
     **/
    @Override
    public PageResult pageQuery(RolePageQueryDTO rolePQDTO) {
        //设置分页参数
        PageHelper.startPage(rolePQDTO.getPage(), rolePQDTO.getPageSize());
        //查询数据
        Page< Role> page = roleMapper.pageQuery(rolePQDTO);
        //返回pageResult
        long total = page.getTotal();
        List<Role> records = page.getResult();
        return new PageResult(total, records);
    }
}
