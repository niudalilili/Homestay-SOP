package com.tanyde.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.constant.MessageConstant;
import com.tanyde.constant.StatusConstant;
import com.tanyde.dto.LoginDTO.EmployeeDTO;
import com.tanyde.dto.LoginDTO.EmployeeLoginDTO;
import com.tanyde.dto.LoginDTO.EmployeePageQueryDTO;
import com.tanyde.entity.LoginPO.Employee;
import com.tanyde.exception.AccountNotFoundException;
import com.tanyde.exception.PasswordErrorException;
import com.tanyde.mapper.EmployeeMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.RedisService;
import com.tanyde.vo.EmployeePageVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.DigestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * EmployeeServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private RedisService redisService;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    /**
     * 验证账号不存在时登录失败
     */
    @Test
    void shouldThrowWhenLoginAccountNotFound() {
        EmployeeLoginDTO dto = new EmployeeLoginDTO();
        dto.setUsername("u1");
        dto.setPassword("123456");
        when(employeeMapper.selectOne(any())).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> employeeService.login(dto));
    }

    /**
     * 验证密码正确且状态可用时登录成功
     */
    @Test
    void shouldLoginWhenPasswordCorrect() {
        EmployeeLoginDTO dto = new EmployeeLoginDTO();
        dto.setUsername("u1");
        dto.setPassword("123456");
        Employee employee = new Employee();
        employee.setUsername("u1");
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setStatus(StatusConstant.ENABLE);
        when(employeeMapper.selectOne(any())).thenReturn(employee);

        Employee result = employeeService.login(dto);

        assertEquals("u1", result.getUsername());
    }

    /**
     * 验证新增员工时默认密码与角色关系写入
     */
    @Test
    void shouldSaveEmployeeWithDefaultPassword() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setUsername("tom");
        dto.setName("Tom");
        dto.setRoleId(3L);
        when(employeeMapper.insert(any(Employee.class))).thenAnswer(invocation -> {
            Employee employee = invocation.getArgument(0);
            employee.setId(88L);
            return 1;
        });

        employeeService.save(dto);

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeMapper).insert(captor.capture());
        assertEquals(StatusConstant.ENABLE, captor.getValue().getStatus());
        verify(employeeMapper).addEmployeeRole(88L, 3L);
    }

    /**
     * 验证员工分页查询返回结果
     */
    @Test
    void shouldReturnEmployeePage() {
        EmployeePageQueryDTO dto = new EmployeePageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(5);
        Page<EmployeePageVO> page = new Page<>(1, 5);
        page.setTotal(2);
        page.setRecords(List.of(new EmployeePageVO(), new EmployeePageVO()));
        when(employeeMapper.pageQuery(any(Page.class), eq(dto))).thenReturn(page);

        PageResult result = employeeService.pageQuery(dto);

        assertEquals(2, result.getTotal());
        assertEquals(2, result.getRecords().size());
    }
}
