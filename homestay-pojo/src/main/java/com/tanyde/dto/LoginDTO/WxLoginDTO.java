package com.tanyde.dto.LoginDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "微信登录请求参数")
public class WxLoginDTO implements Serializable {
    // 微信用户唯一标识
    @Schema(description = "微信openid")
    private String openid;
    @Schema(description = "微信登录code")
    private String code;
}
