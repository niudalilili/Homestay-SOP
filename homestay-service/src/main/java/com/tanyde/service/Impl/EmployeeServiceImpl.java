package com.tanyde.service.Impl;


import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tanyde.constant.MessageConstant;
import com.tanyde.constant.RedisConstant;
import com.tanyde.constant.StatusConstant;
import com.tanyde.dto.LoginDTO.*;
import com.tanyde.entity.LoginPO.Employee;
import com.tanyde.exception.*;
import com.tanyde.mapper.EmployeeMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.EmployeeService;
import com.tanyde.service.RedisService;
import com.tanyde.vo.EmployeePageVO;
import com.tanyde.vo.EmployeeVO;
import com.tanyde.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return com.tanyde.entity.LoginPO.Employee
     * @author TanyDe
     * @create 2026/1/3
     **/
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1.查询用户名数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);
        //2.处理各种异常
        //账号不存在
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //密码对比
        //对前端传来密码进行MD5签名
        if (employee.getPassword() != null) {
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            if (!password.equals(employee.getPassword())) {
                //密码错误
                throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
            }
        } else {
            throw new PasswordErrorException(MessageConstant.PASSWORD_EMPTY);
        }
        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //3.返回实体对象
        return employee;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee wxLogin(String openid) {
        // 校验openid
        if (openid == null || openid.isEmpty()) {
            throw new BaseException("openid不能为空");
        }
        // 查询是否已存在
        Employee employee = employeeMapper.getByOpenid(openid);
        if (employee == null) {
            // 不存在则创建新用户
            employee = Employee.builder()
                    .username(openid)
                    .name("微信用户")
                    .openid(openid)
                    .loginType(1)
                    .status(StatusConstant.ENABLE)
                    .build();
            employeeMapper.insert(employee);
            //赋予用户角色
            employeeMapper.addEmployeeRole(employee.getId(), 7L);
        }
        // 返回登录用户
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
    @Transactional(rollbackFor = Exception.class)
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);
        //获取当前用户id
        Long roleId = employeeDTO.getRoleId();

        //密码加密
        if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
            employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        } else {
            //密码为空则设置默认密码123456
            employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        }
        
        //设置默认登录类型
        if (employee.getLoginType() == null) {
            employee.setLoginType(0);
        }
        //设置默认状态
        if(employee.getStatus() == null){
            employee.setStatus(StatusConstant.ENABLE);
        }

        //添加员工
        employeeMapper.insert(employee);
        //添加员工角色关系
        employeeMapper.addEmployeeRole(employee.getId(), roleId);
    }

    /**
     * 分页查询
     *
     * @param employeePQDTO
     * @return com.tanyde.result.PageResult
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePQDTO) {
        //分页查询
        PageHelper.startPage(employeePQDTO.getPage(), employeePQDTO.getPageSize());
        Page<EmployeePageVO> page = employeeMapper.pageQuery(employeePQDTO);

        long total = page.getTotal();
        List<EmployeePageVO> records = page.getResult();
        return new PageResult(total, records);
    }

    /**
     * 启用禁用员工账号
     *
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return com.tanyde.entity.LoginPO.Employee
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Override
    public EmployeeVO getById(Long id) {
        //获得员工信息
        Employee employee = employeeMapper.getById(id);
        //获得员工角色信息
        Long roleId = employeeMapper.getRoleIdById(id);
        //封装成VO
        EmployeeVO employeeVO = new EmployeeVO();
        BeanUtils.copyProperties(employee, employeeVO);
        employeeVO.setRoleId(roleId);

        return employeeVO;
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
    @Transactional(rollbackFor = Exception.class)
    public void update(EmployeeDTO employeeDTO) {
        //创建员工对象
        Employee employee = new Employee();
        //拷贝属性
        BeanUtils.copyProperties(employeeDTO, employee);
        //修改员工信息
        employeeMapper.update(employee);
        //修改员工角色关系
        Long roleId = employeeDTO.getRoleId();
        Long employeeId = employeeDTO.getId();
        employeeMapper.updateEmployeeRole(employeeId, roleId);
        //清除缓存
        redisService.delete(RedisConstant.EMPLOYEE_ROLE_PREFIX + employeeId);
        redisService.delete(RedisConstant.EMPLOYEE_PERMISSION_PREFIX + employeeId);

    }

    /**
     * 更新用户姓名
     *
     * @param userId 用户ID
     * @param name 用户姓名
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserName(Long userId, String name) {
        Employee employee = Employee.builder()
                .id(userId)
                .name(name)
                .build();
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
        Employee employee = employeeMapper.getById(passwordEditDTO.getEmpId());
        //将用户输入的旧密码进行MD5签名方便对比
        String oldPasswordMD5 = DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes());
        //账号不存在
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //若相同则进行修改密码逻辑，否则抛出异常
        if (employee.getPassword().equals(oldPasswordMD5)) {
            //将之前取出的employee中的密码改为MD5签名后的新密码
            employee.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
            employeeMapper.update(employee);
        } else {
            //抛出密码修改错误异常
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_EDIT_FAILED);
        }
    }

    /**
     * 删除员工
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
        //删除员工
        employeeMapper.deleteById(id);
        //删除员工角色关系
        employeeMapper.deleteEmployeeRoleById(id);
        //清缓存
        redisService.delete(RedisConstant.EMPLOYEE_ROLE_PREFIX + id);
        redisService.delete(RedisConstant.EMPLOYEE_PERMISSION_PREFIX + id);
    }

    /**
     * 更新用户头像
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(UserAvatarUpdateDTO dto) {
        //校验是否为空
        if(dto == null || dto.getAvatar() == null || dto.getAvatar().trim().isEmpty()){
            throw new BaseException("头像地址不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        //组装查表
        Employee  employee = Employee.builder()
                .id(userId)
                .avatar(dto.getAvatar())
                .build();
        employeeMapper.update(employee);

    }
}
