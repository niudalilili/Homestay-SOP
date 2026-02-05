package com.tanyde.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    private String username;

    private String role;

    private String password;

    private String name;

}
