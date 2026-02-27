package com.tanyde.mapper;


import com.github.pagehelper.Page;
import com.tanyde.annotation.AutoFill;
import com.tanyde.dto.LoginDTO.EmployeePageQueryDTO;
import com.tanyde.entity.LoginPO.Employee;
import com.tanyde.enumeration.OperationType;
import com.tanyde.vo.EmployeePageVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询数据
     *
     * @param username
     * @return com.tanyde.entity.LoginPO.Employee
     * @author TanyDe
     * @create 2026/1/3
     **/
    Employee getByUsername(String username);


    /**
     * 插入员工数据
     *
     * @param employee
     * @return void
     * @author TanyDe
     * @create 2026/1/4
     **/
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 添加员工角色信息
     *
     * @param id
     * @param roleId
     * @return void
     * @date 2026/2/23 23:08
     **/
    void addEmployeeRole(@Param("id")Long id, @Param("roleId") Long roleId);

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return com.github.pagehelper.Page<com.tanyde.entity.LoginPO.Employee>
     * @author TanyDe
     * @create 2026/1/4
     **/
    Page<EmployeePageVO> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工数据
     *
     * @param employee
     * @return void
     * @author TanyDe
     * @create 2026/1/4
     **/
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return com.tanyde.entity.LoginPO.Employee
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Select("select * from employee where id=#{id}")
    Employee getById(Long id);

    /**
     * 根据id删除员工
     *
     * @param id
     */
    @Delete("delete from employee where id = #{id}")
    void deleteById(Long id);

    /**
     * 统计员工数量
     *
     * @return
     */
    @Select("select count(*) from employee")
    Integer count();

    /**
     * 根据id查询员工角色信息
     *
     * @return
     */
    Long getRoleIdById(Long employeeId);

    /**
     * 修改员工角色信息
     *
     * @return
     */
    void updateEmployeeRole(@Param("employeeId") Long employeeId,
                            @Param("roleId") Long roleId);

    /**
     * 根据角色id查询员工数量
     *
     * @return
     */
    Integer countByRoleId(Long id);

    /**
     * 根据员工id删除员工角色信息
     *
     * @return
     */
    void deleteEmployeeRoleById(Long id);
}
