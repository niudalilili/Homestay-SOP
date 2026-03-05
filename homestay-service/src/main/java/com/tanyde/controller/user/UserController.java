package com.tanyde.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import com.tanyde.result.Result;
import com.tanyde.service.EmployeeService;
import com.tanyde.service.RoleService;
import com.tanyde.vo.EmployeeVO;
import com.tanyde.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/userInfo")
    @Operation(summary = "获取当前用户信息")
    public Result<UserInfoVO> getUserInfo() {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 查询用户与角色信息
        EmployeeVO employeeVO = employeeService.getById(userId);
        String role = roleService.getRoleCodeByEmployeeId(userId);
        // 组装返回数据
        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(employeeVO.getId())
                .name(employeeVO.getName())
                .role(role)
                .empId(String.valueOf(employeeVO.getId()))
                .build();
        return Result.success(userInfoVO);
    }
}
