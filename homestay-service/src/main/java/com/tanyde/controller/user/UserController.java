package com.tanyde.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import com.tanyde.dto.LoginDTO.UserAvatarUpdateDTO;
import com.tanyde.dto.LoginDTO.UserNameUpdateDTO;
import com.tanyde.exception.BaseException;
import com.tanyde.result.Result;
import com.tanyde.service.EmployeeService;
import com.tanyde.service.RoleService;
import com.tanyde.vo.EmployeeVO;
import com.tanyde.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
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
                .avatar(employeeVO.getAvatar())
                .empId(String.valueOf(employeeVO.getId()))
                .build();
        return Result.success(userInfoVO);
    }

    /**
     * 更新当前用户姓名
     *
     * @param userNameUpdateDTO 更新参数
     * @return 当前用户信息
     */
    @PutMapping("/userInfo")
    @Operation(summary = "更新当前用户姓名")
    public Result<UserInfoVO> updateUserInfo(@RequestBody UserNameUpdateDTO userNameUpdateDTO) {
        String name = userNameUpdateDTO.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new BaseException("用户名不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        employeeService.updateUserName(userId, name.trim());
        EmployeeVO employeeVO = employeeService.getById(userId);
        String role = roleService.getRoleCodeByEmployeeId(userId);
        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(employeeVO.getId())
                .name(employeeVO.getName())
                .role(role)
                .avatar(employeeVO.getAvatar())
                .empId(String.valueOf(employeeVO.getId()))
                .build();
        return Result.success(userInfoVO);
    }

    /**
     * 更新当前用户头像
     *
     * @param dto
     * @return 当前用户信息
     */
    @PutMapping("/avatar")
    @Operation(summary = "更新当前用户头像")
    public Result updateAvatar(@RequestBody UserAvatarUpdateDTO dto){
        log.info("更新用户头像：{}", dto.getAvatar());
        employeeService.updateAvatar(dto);
        return  Result.success();
    }
}
