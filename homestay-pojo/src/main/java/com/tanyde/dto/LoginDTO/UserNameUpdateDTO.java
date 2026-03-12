package com.tanyde.dto.LoginDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户姓名更新参数
 */
@Data
@Schema(description = "用户姓名更新参数")
public class UserNameUpdateDTO implements Serializable {
    /**
     * 用户姓名
     */
    @Schema(description = "用户姓名")
    private String name;
}
