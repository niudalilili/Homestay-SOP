package com.tanyde.service;

import com.tanyde.dto.LoginDTO.EmployeeDTO;
import com.tanyde.dto.LoginDTO.EmployeeLoginDTO;
import com.tanyde.dto.LoginDTO.EmployeePageQueryDTO;
import com.tanyde.dto.LoginDTO.PasswordEditDTO;
import com.tanyde.entity.LoginPO.Employee;
import com.tanyde.result.PageResult;
import com.tanyde.vo.EmployeeVO;

public interface EmployeeService {

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return com.tanyde.entity.LoginPO.Employee
     * @author TanyDe
     * @create 2026/1/3
     **/
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 微信登录
     *
     * @param openid 微信openid
     * @return 登录用户
     */
    Employee wxLogin(String openid);


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
     * 启用禁用员工账号
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return com.tanyde.entity.LoginPO.Employee
     * @author TanyDe
     * @create 2026/1/4
     **/
    EmployeeVO getById(Long id);


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
     * 更新用户姓名
     *
     * @param userId 用户ID
     * @param name 用户姓名
     */
    void updateUserName(Long userId, String name);

    /**
     * 编辑员工密码
     *
     * @param passwordEditDTO
     * @return void
     * @author TanyDe
     * @create 2026/1/4
     **/
    void editPassword(PasswordEditDTO passwordEditDTO);

    /**
     * 删除员工
     *
     * @param id
     */
    void deleteById(Long id);
}
