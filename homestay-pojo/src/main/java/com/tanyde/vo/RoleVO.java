package com.tanyde.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO implements Serializable {
    private Long id;

    private String name;

    private String code;

    private String description;

    private Integer status;
    //权限ids
    private List<Long> permissionIds;
}
