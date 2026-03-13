package com.tanyde.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tanyde.constant.RedisConstant;
import com.tanyde.dto.LoginDTO.RoleDTO;
import com.tanyde.dto.LoginDTO.RolePageQueryDTO;
import com.tanyde.entity.LoginPO.Role;
import com.tanyde.mapper.EmployeeMapper;
import com.tanyde.mapper.RoleMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.RedisService;
import com.tanyde.service.RoleService;
import com.tanyde.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tanyde.exception.BaseException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RedisService redisService;

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
            throw new BaseException("角色编码已存在");
        }
        // 添加角色
        roleMapper.add(role);
        // 添加角色权限关系
        //如果权限为空（棍母角色）,就不写入数据库
        if (permissionIds != null && !permissionIds.isEmpty()) {
            roleMapper.addRolePermission(role.getId(), permissionIds);
        }
        //清除缓存
        clearRoleCaches();
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
        //清除缓存
        clearRoleCaches();
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
        //获取角色信息
        Role role = roleMapper.getById(id);
        //检查是否还有此角色的用户并且是否为超级管理员
        if (employeeMapper.countByRoleId(id) > 0) {
            throw new BaseException("此角色下有员工，不能删除");
        } else if ("super_admin".equals(role.getCode())) {
            throw new BaseException("超级管理员不能删除");
        } else {
            // 删除角色
            roleMapper.deleteRole(id);
            // 删除角色权限关系
            roleMapper.deleteRolePermission(id);
        }
        //清除缓存
        clearRoleCaches();
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
        // 从缓存中获取角色信息
        String cacheKey = RedisConstant.ROLE_DETAIL_PREFIX + roleId;
        Object cached = redisService.get(cacheKey);
        if (cached instanceof RoleVO) {
            return (RoleVO) cached;
        }
        // 获取角色id和权限ids
        Role role = roleMapper.getById(roleId);
        List<Long> permissionIds = roleMapper.getPermissionIdsByRoleId(roleId);
        // 封装数据
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role, roleVO);
        roleVO.setPermissionIds(permissionIds);
        // 缓存角色信息
        redisService.set(cacheKey, roleVO, RedisConstant.ROLE_DETAIL_TTL, TimeUnit.SECONDS);
        return roleVO;
    }

    /**
     * 根据员工id查询角色
     *
     * @param employeeId
     * @return java.lang.String
     * @date 2026/2/23 20:32
     **/
    @Override
    public String getRoleCodeByEmployeeId(Long employeeId) {
        // 从缓存中获取角色信息
        String cacheKey = RedisConstant.EMPLOYEE_ROLE_PREFIX + employeeId;
        Object cached = redisService.get(cacheKey);
        if (cached instanceof String) {
            return (String) cached;
        }
        // 数据库中获取员工角色
        String roleCode = roleMapper.getRoleCodeByEmployeeId(employeeId);
        // 缓存角色信息
        redisService.set(cacheKey, roleCode, RedisConstant.EMPLOYEE_ROLE_TTL, TimeUnit.SECONDS);
        return roleCode;
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
        Integer pageIndex = rolePQDTO.getPage() == null ? 1 : rolePQDTO.getPage();
        Integer pageSize = rolePQDTO.getPageSize() == null ? 10 : rolePQDTO.getPageSize();
        PageHelper.startPage(pageIndex, pageSize);
        //查询数据
        Page<Role> page = roleMapper.pageQuery(rolePQDTO);
        //返回pageResult
        long total = page.getTotal();
        List<Role> records = page.getResult();
        return new PageResult(total, records);
    }

    /**
     * 清理缓存
     *
     * @return void
     * @date 2026/2/23 20:32
     **/
    private void clearRoleCaches() {
        redisService.deleteByPattern(RedisConstant.ROLE_PAGE_PREFIX + "*");
        redisService.deleteByPattern(RedisConstant.ROLE_DETAIL_PREFIX + "*");
        redisService.deleteByPattern(RedisConstant.EMPLOYEE_ROLE_PREFIX + "*");
        redisService.deleteByPattern(RedisConstant.EMPLOYEE_PERMISSION_PREFIX + "*");
        redisService.deleteByPattern(RedisConstant.PERMISSION_TREE_PREFIX + "*");
        redisService.deleteByPattern(RedisConstant.PERMISSION_LIST_PREFIX + "*");
    }
}
