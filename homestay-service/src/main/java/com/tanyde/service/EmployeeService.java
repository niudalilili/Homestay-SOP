package com.tanyde.service;

import com.tanyde.dto.EmployeeLoginDTO;
import com.tanyde.entity.Employee;

public interface EmployeeService {

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return com.tanyde.entity.Employee
     * @author TanyDe
     * @create 2026/1/3
     **/
    Employee login(EmployeeLoginDTO employeeLoginDTO);


}
