package com.tanyde.mapper;


import com.tanyde.entity.Employee;
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




}
