package com.tanyde.dto.LoginDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionPageQueryDTO implements Serializable {

    private String name;

    private String code;

    private String description;
    //页码
    private Integer page;
    //每页显示记录数
    private Integer pageSize;

}
