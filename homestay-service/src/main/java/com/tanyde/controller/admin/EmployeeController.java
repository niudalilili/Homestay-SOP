package com.tanyde.controller.admin;


import com.tanyde.constant.JwtClaimsConstant;
import com.tanyde.dto.EmployeeDTO;
import com.tanyde.dto.EmployeeLoginDTO;
import com.tanyde.dto.EmployeePageQueryDTO;
import com.tanyde.dto.PasswordEditDTO;
import com.tanyde.entity.Employee;
import com.tanyde.properties.JwtProperties;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.EmployeeService;
import com.tanyde.utils.JwtUtil;
import com.tanyde.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 *
 * @author TanyDe
 * @create 2026/1/2
 **/
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return com.tanyde.result.Result<com.tanyde.vo.EmployeeLoginVO>
     * @author TanyDe
     * @create 2026/1/3
     **/
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录:{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功，生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT
                (jwtProperties.getAdminSecretKey(),
                        jwtProperties.getAdminTtl(),
                        claims);
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
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
    @PostMapping("/logout")
    public Result<String> logout(){
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
@ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工:{}",employeeDTO);
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
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数为{}",employeePageQueryDTO);
        PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);
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
    @ApiOperation("启用禁用员工账号")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用员工账号:{}{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return com.tanyde.result.Result<com.tanyde.entity.Employee>
     * @author TanyDe
     * @create 2026/1/4
     **/
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    public Result<Employee>getById(@PathVariable Long id){
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
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
    @ApiOperation("编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息:{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * 编辑员工密码
     * param passwordEditDTO
     * @return com.tanyde.result.Result
     * @author TanyDe
     * @create 2026/1/4
     **/
    @PutMapping("/editPassword")
    @ApiOperation(("编辑员工密码"))
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO){
        log.info("编辑员工密码:{}",passwordEditDTO);
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }




}
