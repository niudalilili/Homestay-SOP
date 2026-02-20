package com.tanyde.service.Impl;


import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tanyde.constant.MessageConstant;
import com.tanyde.constant.PasswordConstant;
import com.tanyde.constant.StatusConstant;
import com.tanyde.dto.EmployeeDTO;
import com.tanyde.dto.EmployeeLoginDTO;
import com.tanyde.dto.EmployeePageQueryDTO;
import com.tanyde.dto.PasswordEditDTO;
import com.tanyde.entity.Employee;
import com.tanyde.exception.*;
import com.tanyde.mapper.EmployeeMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return com.tanyde.entity.Employee
     * @author TanyDe
     * @create 2026/1/3
     **/
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username= employeeLoginDTO.getUsername();
        String password= employeeLoginDTO.getPassword();

        //1.查询用户名数据库中的数据
        Employee employee=employeeMapper.getByUsername(username);
        //2.处理各种异常
            //账号不存在
        if(employee==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //密码对比
        //对前端传来密码进行MD5签名
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        if(!password.equals(employee.getPassword())){
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        if(employee.getStatus().equals(StatusConstant.DISABLE)){
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //3.返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return void
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();

        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);

        //密码加密
        if (employee.getPassword() != null) {
            employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        }

        //设置账号状态
        employee.setStatus(StatusConstant.ENABLE);

        employeeMapper.insert(employee);
    }

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return com.tanyde.result.PageResult
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);

        long total=page.getTotal();
        List<Employee> records=page.getResult();
        return new PageResult(total,records);
    }

    /**
     *启用禁用员工账号
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee=Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return com.tanyde.entity.Employee
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Override
    public Employee getById(Long id) {
        Employee employee=employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO
     * @return void
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee =new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        employeeMapper.update(employee);
    }

    /**
     * 编辑员工密码
     *
     * @param passwordEditDTO
     * @return void
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        //根据id获得信息
        Employee employee=employeeMapper.getById(passwordEditDTO.getEmpId());
        //将用户输入的旧密码进行MD5签名方便对比
        String oldPasswordMD5= DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes());
        //若相同则进行修改密码逻辑，否则抛出异常
        if(employee.getPassword().equals(oldPasswordMD5)){
            //将之前取出的employee中的密码改为MD5签名后的新密码
            employee.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
            employeeMapper.update(employee);
        }else{
            //抛出密码修改错误异常
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_EDIT_FAILED);
        }
    }

    /**
     * 删除员工
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        // 1. 查询员工信息
        Employee employee = employeeMapper.getById(id);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 2. 校验：不能删除admin账号
        if ("admin".equals(employee.getUsername())) {
            throw new BaseException(MessageConstant.ADMIN_DELETE_DENIED);
        }

        // 3. 校验：不能删除当前登录用户
        Long currentId = StpUtil.getLoginIdAsLong();
        if (id.equals(currentId)) {
            throw new BaseException(MessageConstant.SELF_DELETE_DENIED);
        }

        // 4. 校验：不能删除最后一个用户
        Integer count = employeeMapper.count();
        if (count <= 1) {
            throw new BaseException(MessageConstant.LAST_USER_DELETE_DENIED);
        }

        // 5. 执行删除
        employeeMapper.deleteById(id);
    }
}
