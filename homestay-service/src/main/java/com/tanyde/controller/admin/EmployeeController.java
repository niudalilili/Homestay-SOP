package com.tanyde.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;

import cn.dev33.satoken.stp.StpUtil;
import com.tanyde.dto.LoginDTO.EmployeeDTO;
import com.tanyde.dto.LoginDTO.EmployeeLoginDTO;
import com.tanyde.dto.LoginDTO.EmployeePageQueryDTO;
import com.tanyde.dto.LoginDTO.PasswordEditDTO;
import com.tanyde.entity.LoginPO.Employee;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.EmployeeService;
import com.tanyde.vo.EmployeeLoginVO;
import com.tanyde.vo.EmployeeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理
 *
 * @author TanyDe
 * @create 2026/1/2
 **/
@RestController
@RequestMapping("/admin/employee")
@Tag(name = "员工管理", description = "员工管理相关接口")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return com.tanyde.result.Result<com.tanyde.vo.EmployeeLoginVO>
     * @author TanyDe
     * @create 2026/1/3
     **/
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录:{}", employeeLoginDTO);
        // 1. 调用service进行登录密码校验
        Employee employee = employeeService.login(employeeLoginDTO);

        // 2.登录成功，使用satoken记录登录状态
        // 参数填写用户id，生成token并注入到cookie/header
        StpUtil.login(employee.getId());
        // 3.构建返回结果
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(StpUtil.getTokenValue())
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return com.tanyde.result.Result<java.lang.String>
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Operation(summary = "员工退出登录 ")
    @PostMapping("/logout")
    public Result<String> logout() {
        StpUtil.logout();
        log.info("退出登录");
        return Result.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return com.tanyde.result.Result<java.lang.String>
     * @author TanyDe
     * @create 2026/1/4
     **/
    @PostMapping
    @Operation(summary = "新增员工")
    @SaCheckPermission("employee:add")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工:{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     * 
     * @param employeePageQueryDTO
     * @return
     * @author TanyDe
     * @create 2026/1/4
     **/
    @GetMapping("/page")
    @Operation(summary = "员工分页查询")
    @SaCheckPermission("employee:query")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询，参数为{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工账号
     * 
     * @param status
     * @return
     * @author TanyDe
     * @create 2026/1/4
     **/
    @PostMapping("/status/{status}")
    @Operation(summary = "启用禁用员工账号")
    @SaCheckPermission("employee:status")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用禁用员工账号:{}{}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据id查询员工信息
     * 
     * @param id
     * @return com.tanyde.result.Result<com.tanyde.entity.LoginPO.Employee>
     * @author TanyDe
     * @create 2026/1/4
     **/
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询员工信息")
    @SaCheckPermission("employee:query")
    public Result<EmployeeVO> getById(@PathVariable Long id) {
        EmployeeVO employeeVO = employeeService.getById(id);
        return Result.success(employeeVO);
    }

    /**
     * 编辑员工信息
     * 
     * @param employeeDTO
     * @return
     * @author TanyDe
     * @create 2026/1/4
     **/
    @PutMapping
    @Operation(summary = "编辑员工信息")
    @SaCheckPermission("employee:update")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息:{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * 编辑员工密码
     * 
     * @param passwordEditDTO
     * @return com.tanyde.result.Result
     * @author TanyDe
     * @create 2026/1/4
     **/
    @PutMapping("/editPassword")
    @Operation(summary = "编辑员工密码")
    @SaCheckPermission("employee:password")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("编辑员工密码:{}", passwordEditDTO);
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }

    /**
     * 删除员工
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除员工")
    @SaCheckPermission("employee:delete")
    public Result delete(@PathVariable Long id) {
        log.info("删除员工:{}", id);
        employeeService.deleteById(id);
        return Result.success();
    }

}
