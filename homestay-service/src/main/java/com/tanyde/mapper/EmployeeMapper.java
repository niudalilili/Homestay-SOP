package com.tanyde.mapper;


import com.github.pagehelper.Page;
import com.tanyde.annotation.AutoFill;
import com.tanyde.dto.EmployeePageQueryDTO;
import com.tanyde.entity.Employee;
import com.tanyde.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询数据
     * 
     * @param username
     * @return com.tanyde.entity.Employee
     * @author TanyDe
     * @create 2026/1/3
     **/
    @Select("select * from employee where username=#{username}")
    Employee getByUsername(String username);


    /**
     * 插入员工数据
     *
     * @param employee
     * @return void
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Insert("insert into employee(username, name, password, status, create_time, update_time, create_user, update_user) "+
    "values "+
    "(#{username}, #{name}, #{password}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value= OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 分页查询
     *
    * @param employeePageQueryDTO
     * @return com.github.pagehelper.Page<com.tanyde.entity.Employee>
     * @author TanyDe
     * @create 2026/1/4
     **/
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工数据
     *
     * @param employee
     * @return void
     * @author TanyDe
     * @create 2026/1/4
     **/
    @AutoFill(value=OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据id查询员工信息
     * 
     * @param id
     * @return com.tanyde.entity.Employee
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Select("select * from employee where id=#{id}")
    Employee getById(Long id);
}
