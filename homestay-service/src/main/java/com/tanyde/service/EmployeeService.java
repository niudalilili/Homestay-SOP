package com.tanyde.service;

import com.tanyde.dto.EmployeeDTO;
import com.tanyde.dto.EmployeeLoginDTO;
import com.tanyde.dto.EmployeePageQueryDTO;
import com.tanyde.dto.PasswordEditDTO;
import com.tanyde.entity.Employee;
import com.tanyde.result.PageResult;

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


    /**
     * 保存员工信息
     *
     * @param employeeDTO
     * @return void
     * @author TanyDe
     * @create 2026/1/3
     **/
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return com.tanyde.result.PageResult
     * @author TanyDe
     * @create 2026/1/4
     **/
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     *启用禁用员工账号
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return com.tanyde.entity.Employee
     * @author TanyDe
     * @create 2026/1/4
     **/
    Employee getById(Long id);

    /**
 * 编辑员工信息
 *
 * @param employeeDTO
 * @return void
 * @author TanyDe
 * @create 2026/1/4
 **/
    void update(EmployeeDTO employeeDTO);

    /**
     * 编辑员工密码
     *
     * @param passwordEditDTO
     * @return void
     * @author TanyDe
     * @create 2026/1/4
     **/
    void editPassword(PasswordEditDTO passwordEditDTO);
}
