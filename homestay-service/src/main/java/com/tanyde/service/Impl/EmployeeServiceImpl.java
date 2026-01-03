package com.tanyde.service.Impl;


import com.tanyde.constant.MessageConstant;
import com.tanyde.constant.StatusConstant;
import com.tanyde.dto.EmployeeLoginDTO;
import com.tanyde.entity.Employee;
import com.tanyde.exception.AccountLockedException;
import com.tanyde.exception.AccountNotFoundException;
import com.tanyde.exception.PasswordErrorException;
import com.tanyde.mapper.EmployeeMapper;
import com.tanyde.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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
}
