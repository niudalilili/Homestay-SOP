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
@Schema(description = "认证登录返回数据")
public class AuthLoginVO implements Serializable {
    // Token 字符串
    @Schema(description = "Token")
    private String token;

    // 用户信息
    @Schema(description = "用户信息")
    private UserInfoVO user;
}
