package com.tanyde.dto.LoginDTO;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String name;

    private Integer status;

    private Long roleId;

    private String avatar;

}
