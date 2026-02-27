package com.tanyde.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import com.tanyde.result.Result;
import com.tanyde.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.tanyde.vo.PermissionGroupVO;

/**
 * 权限管理
 *
 * @author TanyDe
 * @create 2026/2/24
 **/
@RestController
@RequestMapping("/admin/permission")
@Tag(name = "权限管理", description = "权限管理相关接口")
@Slf4j
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取权限树
     *
     * @return com.tanyde.result.Result<java.util.List<com.tanyde.vo.PermissionGroupVO>>
     * @date 2026/2/24
     **/
    @GetMapping("/tree")
    @Operation(summary = "获取权限树")
    public Result<List<PermissionGroupVO>> getPermissionTree() {
        List<PermissionGroupVO> tree = permissionService.getTree();
        return Result.success(tree);
    }

    /**
     * 获取当前用户权限列表
     *
     * @return com.tanyde.result.Result<java.util.List<java.lang.String>>
     * @date 2026/2/24
     **/
    @GetMapping("/current")
    @Operation(summary = "获取当前用户权限列表")
    public Result<List<String>> getCurrentUserPermissions() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<String> permissions = permissionService.getCodesByIds(userId);
        log.info("获取当前用户权限列表: {}", permissions);
        return Result.success(permissions);
    }
}
