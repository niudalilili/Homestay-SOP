package com.tanyde.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeVO {
    private static final long serialVersionUID =2L;

    private Long id;

    private String username;

    private String name;

    private Integer status;

    private Long roleId;

    private String avatar;
}
