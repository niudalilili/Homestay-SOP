package com.tanyde.controller.admin;

import com.tanyde.dto.LoginDTO.RoleDTO;
import com.tanyde.dto.LoginDTO.RolePageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.RoleService;
import com.tanyde.vo.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/role")
@Slf4j
@Tag(name = "角色管理", description = "角色管理相关接口")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 添加角色
     *
     * @param roleVO
     * @return com.tanyde.result.Result
     * @date 2026/2/23 20:05
     **/
    @PostMapping
    @Operation(summary = "添加角色")
    public Result addRole(RoleVO roleVO) {
        roleService.add(roleVO);
        log.info("添加角色:{}", roleVO);

        return Result.success();
    }

    /**
     * 修改角色
     *
     * @param roleVO
     * @return com.tanyde.result.Result
     * @date 2026/2/23 20:06
     **/
    @PostMapping("/update")
    @Operation(summary = "修改角色")
    public Result updateRole(RoleVO roleVO) {
        roleService.update(roleVO);
        log.info("修改角色:{}", roleVO);

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
    public Result deleteRole(Long id) {
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
    public Result<RoleDTO> selectById(Long id) {
        RoleDTO roleDTO = roleService.getById(id);
        log.info("根据id:{}查询角色:{}", id, roleDTO);

        return Result.success(roleDTO);
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
    public PageResult selectPage(RolePageQueryDTO rolePQDTO) {
        PageResult pageResult = roleService.pageQuery(rolePQDTO);
        log.info("分页查询角色:{}", rolePQDTO);
        log.info("分页查询结果:{}", pageResult);

        return pageResult;
    }

}


