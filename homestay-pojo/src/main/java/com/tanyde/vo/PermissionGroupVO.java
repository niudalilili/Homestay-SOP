package com.tanyde.vo;

import com.tanyde.entity.LoginPO.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionGroupVO implements Serializable {
    private String module;
    private List<Permission> permissions;
}
