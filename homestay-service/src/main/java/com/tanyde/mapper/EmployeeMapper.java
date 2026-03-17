package com.tanyde.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.dto.LoginDTO.EmployeePageQueryDTO;
import com.tanyde.entity.LoginPO.Employee;
import com.tanyde.vo.EmployeePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

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
     * 根据openid查询用户
     *
     * @param openid 微信openid
     * @return 用户信息
     */
    Employee getByOpenid(String openid);


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
     * @param page
     * @return com.github.pagehelper.Page<com.tanyde.entity.LoginPO.Employee>
     * @author TanyDe
     * @create 2026/1/4
     **/
    IPage<EmployeePageVO> pageQuery(Page<EmployeePageVO> page,
                                    @Param("dto") EmployeePageQueryDTO employeePageQueryDTO);


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
