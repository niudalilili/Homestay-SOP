package com.tanyde.dto.LoginDTO;


import lombok.Data;

@Data
public class EmployeePageQueryDTO {
    //员工姓名
    private String name;
    //员工角色
    private String role;
    //员工账号
    private String username;
    //页码
    private int page;
    //每页显示记录数
    private int pageSize;

}
