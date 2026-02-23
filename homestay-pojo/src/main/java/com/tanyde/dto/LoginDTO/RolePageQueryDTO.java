package com.tanyde.dto.LoginDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePageQueryDTO implements Serializable {
    private Long id;

    private String name;

    private String code;

    private String description;

    private Integer status;

    private Integer page;

    private Integer pageSize;
}
