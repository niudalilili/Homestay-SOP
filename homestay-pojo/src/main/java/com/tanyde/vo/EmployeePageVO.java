package com.tanyde.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "员工分页查询VO")
public class EmployeePageVO implements Serializable {
    private Long id;

    private String username;

    private String name;

    private Integer status;

    @Schema(description = "角色名称")
    private String roleName;


}
