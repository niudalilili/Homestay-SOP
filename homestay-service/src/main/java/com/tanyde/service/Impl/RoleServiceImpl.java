package com.tanyde.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tanyde.dto.LoginDTO.RoleDTO;
import com.tanyde.dto.LoginDTO.RolePageQueryDTO;
import com.tanyde.entity.LoginPO.Role;
import com.tanyde.mapper.EmployeeMapper;
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
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 添加角色
     *
     * @param roleDTO
     * @return void
     * @date 2026/2/23 20:29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(RoleDTO roleDTO) {
        // 获取权限id
        List<Long> permissionIds = roleDTO.getPermissionIds();
        // 封装角色数据
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        // 检查角色编码是否已存在
        String code = role.getCode();
        if (roleMapper.countByCode(code) > 0) {
            throw new RuntimeException("角色编码已存在");
        }
        // 添加角色
        roleMapper.add(role);
        // 添加角色权限关系
        roleMapper.addRolePermission(role.getId(), permissionIds);
    }

    /**
     * 更新角色
     *
     * @param roleDTO
     * @return void
     * @date 2026/2/23 20:29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleDTO roleDTO) {
        // 获取角色id和权限ids
        Long roleId = roleDTO.getId();
        List<Long> permissionIds = roleDTO.getPermissionIds();
        // 封装角色数据
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        // 更新角色
        roleMapper.update(role);
        // 修改角色权限关系
        roleMapper.deleteRolePermission(roleId);
        roleMapper.addRolePermission(roleId, permissionIds);
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
        //检查是否还有此角色的用户
        if (employeeMapper.countByRoleId(id) > 0) {
            throw new RuntimeException("此角色下有员工，不能删除");
        } else {
            // 删除角色
            roleMapper.deleteRole(id);
            // 删除角色权限关系
            roleMapper.deleteRolePermission(id);
        }
    }

    /**
     * 根据id查询
     *
     * @param roleId
     * @return com.tanyde.dto.LoginDTO.RoleDTO
     * @date 2026/2/23 20:30
     **/
    @Override
    public RoleVO getById(Long roleId) {
        // 获取角色id和权限ids
        Role role = roleMapper.getById(roleId);
        List<Long> permissionIds = roleMapper.getPermissionIdsByRoleId(roleId);
        // 封装数据
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role, roleVO);
        roleVO.setPermissionIds(permissionIds);

        return roleVO;
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
        Page<Role> page = roleMapper.pageQuery(rolePQDTO);
        //返回pageResult
        long total = page.getTotal();
        List<Role> records = page.getResult();
        return new PageResult(total, records);
    }
}
