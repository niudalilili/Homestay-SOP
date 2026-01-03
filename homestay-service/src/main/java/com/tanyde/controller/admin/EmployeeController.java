package com.tanyde.controller.admin;


import com.tanyde.constant.JwtClaimsConstant;
import com.tanyde.dto.EmployeeDTO;
import com.tanyde.dto.EmployeeLoginDTO;
import com.tanyde.entity.Employee;
import com.tanyde.properties.JwtProperties;
import com.tanyde.result.Result;
import com.tanyde.service.EmployeeService;
import com.tanyde.utils.JwtUtil;
import com.tanyde.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 * @author TanyDe
 * @create 2026/1/2
 **/
@RestController
@RequestMapping("/admin/emplyee")
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
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        log.info("员工登录:{}",employeeLoginDTO);

        Employee employee =employeeService.login(employeeLoginDTO);

        //登录成功，生成JWT令牌
        Map<String,Object>claims=new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,employee.getId());
        String token= JwtUtil.createJWT
                (jwtProperties.getAdminSecretKey(),
                 jwtProperties.getAdminTtl(),
                 claims);
        EmployeeLoginVO employeeLoginVO=EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }





}
