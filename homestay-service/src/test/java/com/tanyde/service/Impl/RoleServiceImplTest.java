package com.tanyde.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.constant.RedisConstant;
import com.tanyde.dto.LoginDTO.RoleDTO;
import com.tanyde.dto.LoginDTO.RolePageQueryDTO;
import com.tanyde.entity.LoginPO.Role;
import com.tanyde.exception.BaseException;
import com.tanyde.mapper.EmployeeMapper;
import com.tanyde.mapper.RoleMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.RedisService;
import com.tanyde.vo.RoleVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * RoleServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleMapper roleMapper;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private RedisService redisService;
    @InjectMocks
    private RoleServiceImpl roleService;

    /**
     * 验证角色编码已存在时新增失败
     */
    @Test
    void shouldThrowWhenRoleCodeExists() {
        RoleDTO dto = RoleDTO.builder().code("admin").build();
        when(roleMapper.countByCode("admin")).thenReturn(1);

        assertThrows(BaseException.class, () -> roleService.add(dto));
    }

    /**
     * 验证新增角色后写入角色权限关联
     */
    @Test
    void shouldAddRoleAndPermissions() {
        RoleDTO dto = RoleDTO.builder().code("r1").permissionIds(List.of(1L, 2L)).build();
        when(roleMapper.countByCode("r1")).thenReturn(0);
        when(roleMapper.insert(any(Role.class))).thenAnswer(invocation -> {
            Role role = invocation.getArgument(0);
            role.setId(99L);
            return 1;
        });

        roleService.add(dto);

        verify(roleMapper).addRolePermission(99L, List.of(1L, 2L));
    }

    /**
     * 验证删除超级管理员角色被拒绝
     */
    @Test
    void shouldDeleteRejectWhenSuperAdmin() {
        Role role = new Role();
        role.setCode("super_admin");
        when(roleMapper.selectById(1L)).thenReturn(role);
        when(employeeMapper.countByRoleId(1L)).thenReturn(0);

        assertThrows(BaseException.class, () -> roleService.delete(1L));
    }

    /**
     * 验证角色详情查询与权限列表组装
     */
    @Test
    void shouldReturnRoleDetailAndCache() {
        when(redisService.get(RedisConstant.ROLE_DETAIL_PREFIX + 2L)).thenReturn(null);
        Role role = new Role();
        role.setId(2L);
        role.setCode("editor");
        when(roleMapper.selectById(2L)).thenReturn(role);
        when(roleMapper.getPermissionIdsByRoleId(2L)).thenReturn(List.of(7L));

        RoleVO result = roleService.getById(2L);

        assertEquals("editor", result.getCode());
        assertEquals(1, result.getPermissionIds().size());
    }

    /**
     * 验证角色分页查询结果
     */
    @Test
    void shouldReturnPagedRoles() {
        RolePageQueryDTO dto = RolePageQueryDTO.builder().page(1).pageSize(10).build();
        Page<Role> page = new Page<>(1, 10);
        page.setTotal(3);
        page.setRecords(List.of(new Role(), new Role()));
        when(roleMapper.pageQuery(any(Page.class), eq(dto))).thenReturn(page);

        PageResult result = roleService.pageQuery(dto);

        assertEquals(3, result.getTotal());
    }
}
