package com.tanyde.entity.LoginPO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("permission")
public class Permission implements Serializable {
    private static final long serialVersionUID = 2L;
    @TableId(type = IdType.AUTO)
    private Long id;
    @Schema(description = "权限名称")
    private String name;
    @Schema(description = "权限编码（唯一，如employee:add）")
    private String code;
    @Schema(description = "权限描述")
    private String description;
    @Schema(description = "状态（0-禁止 1-启用）")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
