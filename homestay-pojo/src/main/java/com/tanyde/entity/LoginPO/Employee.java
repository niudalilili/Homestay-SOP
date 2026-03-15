package com.tanyde.entity.LoginPO;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "员工信息")
public class Employee implements Serializable {

    private static final long serialVersionUID =2L;

    private Long id;
    @Schema(description = "用户名（登录账号）")
    private String username;
    @Schema(description = "用户姓名" )
    private String name;
    @Schema(description = "密码（加密存储")
    private String password;
    @Schema(description = "微信openid")
    private String openid;
    @Schema(description = "登录类型：0-传统密码，1-微信openid")
    private Integer loginType;
    @Schema(description = "状态（0-禁用 1-正常）")
    private Integer status;
    @Schema(description = "头像")
    private String avatar;

    private LocalDateTime createTime;

    private  LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
