package com.tanyde.dto.LoginDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO implements Serializable {

    private Long id;

    private String name;

    private String code;

    private String description;

    private Integer status;
}
