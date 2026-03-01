package com.tanyde.entity.LoginPO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "权限信息")
public class Permission implements Serializable {
    private static final long serialVersionUID = 2L;

    private Long id;
    @Schema(description = "权限名称")
    private String name;
    @Schema(description = "权限编码（唯一，如employee:add）")
    private String code;
    @Schema(description = "权限描述")
    private String description;
    @Schema(description = "状态（0-禁止 1-启用）")
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
