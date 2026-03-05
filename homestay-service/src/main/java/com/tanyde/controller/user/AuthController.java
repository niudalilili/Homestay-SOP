package com.tanyde.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import com.tanyde.dto.LoginDTO.WxLoginDTO;
import com.tanyde.entity.LoginPO.Employee;
import com.tanyde.result.Result;
import com.tanyde.service.EmployeeService;
import com.tanyde.service.RoleService;
import com.tanyde.vo.AuthLoginVO;
import com.tanyde.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "认证相关接口")
@Slf4j
public class AuthController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/login")
    @Operation(summary = "微信登录")
    public Result<AuthLoginVO> login(@RequestBody WxLoginDTO wxLoginDTO) {
        // 调用服务完成微信登录或注册
        Employee employee = employeeService.wxLogin(wxLoginDTO.getOpenid());
        // 记录登录状态
        StpUtil.login(employee.getId(), "user");
        // 获取角色信息
        String role = roleService.getRoleCodeByEmployeeId(employee.getId());
        // 构建返回用户信息
        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .role(role)
                .empId(String.valueOf(employee.getId()))
                .build();
        // 返回token与用户信息
        AuthLoginVO authLoginVO = AuthLoginVO.builder()
                .token(StpUtil.getTokenValue())
                .user(userInfoVO)
                .build();
        return Result.success(authLoginVO);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<String> logout() {
        // 清理登录状态
        StpUtil.logout();
        return Result.success("退出成功");
    }
}
