package com.tanyde.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tanyde.dto.LoginDTO.RoleDTO;
import com.tanyde.dto.LoginDTO.RolePageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.RoleService;
import com.tanyde.vo.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/role")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色管理相关接口")
public class RoleController {
    private final RoleService roleService;

    /**
     * 添加角色
     *
     * @param roleDTO
     * @return com.tanyde.result.Result
     * @date 2026/2/23 20:05
     **/
    @PostMapping
    @Operation(summary = "添加角色")
    @SaCheckPermission("system:role:add")
    public Result addRole(@RequestBody RoleDTO roleDTO) {
        roleService.add(roleDTO);
        log.info("添加角色:{}", roleDTO);

        return Result.success();
    }

    /**
     * 修改角色
     *
     * @param roleDTO
     * @return com.tanyde.result.Result
     * @date 2026/2/23 20:06
     **/
    @PostMapping("/update")
    @Operation(summary = "修改角色")
    @SaCheckPermission("system:role:update")
    public Result updateRole(@RequestBody RoleDTO roleDTO) {
        roleService.update(roleDTO);
        log.info("修改角色:{}", roleDTO);

        return Result.success();
    }

    /**
     * 删除角色
     *
     * @param id
     * @return com.tanyde.result.Result
     * @date 2026/2/23 20:07
     **/
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    @SaCheckPermission("system:role:delete")
    public Result deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        log.info("删除角色:{}", id);

        return Result.success();
    }

    /**
     * 根据id查询角色
     *
     * @param id
     * @return com.tanyde.result.Result<com.tanyde.dto.LoginDTO.RoleDTO>
     * @date 2026/2/23 20:18
     **/
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询角色")
    @SaCheckPermission("system:role:query")
    public Result<RoleVO> selectById(@PathVariable Long id) {
        RoleVO roleVO = roleService.getById(id);
        log.info("根据id:{}查询角色:{}", id, roleVO);

        return Result.success(roleVO);
    }

    /**
     * 分页查询角色
     *
     * @param rolePQDTO
     * @return com.tanyde.result.PageResult
     * @date 2026/2/23 20:21
     **/
    @GetMapping("/page")
    @Operation(summary = "分页查询角色")
    @SaCheckPermission("system:role:query")
    public Result<PageResult> selectPage(RolePageQueryDTO rolePQDTO) {
        PageResult pageResult = roleService.pageQuery(rolePQDTO);
        log.info("分页查询角色:{}", rolePQDTO);
        log.info("分页查询结果:{}", pageResult);

        return Result.success(pageResult);
    }

}
