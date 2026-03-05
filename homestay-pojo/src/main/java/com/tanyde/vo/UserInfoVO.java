package com.tanyde.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户信息")
public class UserInfoVO implements Serializable {
    // 用户ID
    @Schema(description = "ID")
    private Long id;

    // 用户姓名
    @Schema(description = "姓名")
    private String name;

    // 角色编码
    @Schema(description = "角色")
    private String role;

    // 头像地址
    @Schema(description = "头像")
    private String avatar;

    // 员工编号
    @Schema(description = "员工编号")
    private String empId;
}
