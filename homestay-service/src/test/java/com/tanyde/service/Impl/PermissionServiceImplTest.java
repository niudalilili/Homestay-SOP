package com.tanyde.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.constant.RedisConstant;
import com.tanyde.dto.LoginDTO.PermissionDTO;
import com.tanyde.dto.LoginDTO.PermissionPageQueryDTO;
import com.tanyde.entity.LoginPO.Permission;
import com.tanyde.mapper.PermissionMapper;
import com.tanyde.mapper.RoleMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.RedisService;
import com.tanyde.vo.PermissionGroupVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * PermissionServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class PermissionServiceImplTest {

    @Mock
    private PermissionMapper permissionMapper;
    @Mock
    private RoleMapper roleMapper;
    @Mock
    private RedisService redisService;
    @InjectMocks
    private PermissionServiceImpl permissionService;

    /**
     * 验证根据 ID 获取权限详情
     */
    @Test
    void shouldGetPermissionById() {
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setCode("employee:view");
        when(permissionMapper.selectById(1L)).thenReturn(permission);

        PermissionDTO dto = permissionService.getById(1L);

        assertEquals("employee:view", dto.getCode());
    }

    /**
     * 验证超级管理员权限读取与缓存写入
     */
    @Test
    void shouldLoadCodesForSuperAdminAndCache() {
        when(redisService.get(RedisConstant.EMPLOYEE_PERMISSION_PREFIX + 2L)).thenReturn(null);
        when(roleMapper.getRoleCodeByEmployeeId(2L)).thenReturn("super_admin");
        when(permissionMapper.listAll()).thenReturn(List.of("a", "b"));

        List<String> codes = permissionService.getCodesByIds(2L);

        assertEquals(2, codes.size());
        verify(redisService).set(RedisConstant.EMPLOYEE_PERMISSION_PREFIX + 2L, codes, RedisConstant.EMPLOYEE_PERMISSION_TTL, TimeUnit.SECONDS);
    }

    /**
     * 验证权限树按模块聚合结果
     */
    @Test
    void shouldBuildPermissionTree() {
        Permission p1 = new Permission();
        p1.setCode("employee:add");
        Permission p2 = new Permission();
        p2.setCode("activity:view");
        when(redisService.get(RedisConstant.PERMISSION_TREE_PREFIX)).thenReturn(null);
        when(permissionMapper.findAll()).thenReturn(List.of(p1, p2));

        List<PermissionGroupVO> tree = permissionService.getTree();

        assertNotNull(tree);
        assertEquals(2, tree.size());
    }

    /**
     * 验证权限分页查询结果
     */
    @Test
    void shouldReturnPagedPermissions() {
        PermissionPageQueryDTO dto = new PermissionPageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);
        when(redisService.get(contains(RedisConstant.PERMISSION_LIST_PREFIX + ":page:"))).thenReturn(null);
        Page<Permission> page = new Page<>(1, 10);
        page.setTotal(1);
        page.setRecords(List.of(new Permission()));
        when(permissionMapper.pageQuery(any(Page.class), any(PermissionPageQueryDTO.class))).thenReturn(page);

        PageResult result = permissionService.pageQuery(dto);

        assertEquals(1, result.getTotal());
    }
}
