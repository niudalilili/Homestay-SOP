package com.tanyde.dto.LoginDTO;


import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String name;

}
