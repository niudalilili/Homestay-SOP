package com.tanyde.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.constant.RedisConstant;
import com.tanyde.dto.ActivityDTO.ActivityPlanPageQueryDTO;
import com.tanyde.entity.ActivityPO.ActivityPlan;
import com.tanyde.exception.BaseException;
import com.tanyde.mapper.ActivityPlanContentMapper;
import com.tanyde.mapper.ActivityPlanMapper;
import com.tanyde.mapper.ActivityStepMapper;
import com.tanyde.mapper.EmployeeMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.RedisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

/**
 * ActivityPlanServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class ActivityPlanServiceImplTest {

    @Mock
    private ActivityPlanMapper activityPlanMapper;
    @Mock
    private ActivityPlanContentMapper activityPlanContentMapper;
    @Mock
    private ActivityStepMapper activityStepMapper;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private RedisService redisService;
    @InjectMocks
    private ActivityPlanServiceImpl activityPlanService;

    /**
     * 验证删除 ID 为空时抛出业务异常
     */
    @Test
    void shouldThrowWhenDeleteIdsEmpty() {
        assertThrows(BaseException.class, () -> activityPlanService.deleteByIds(List.of()));
    }

    /**
     * 测试分页查询功能
     * 验证当缓存未命中时，从数据库查询并返回分页结果
     * 
     * @测试场景 缓存不存在时，执行数据库分页查询
     * @测试步骤 1. 构建分页查询参数（页码 1，每页 10 条）
     *         2. 模拟缓存未命中（返回 null）
     *         3. 模拟数据库查询返回 4 条总记录，2 条具体数据
     *         4. 执行分页查询
     * @预期结果 返回的分页结果包含正确的总数和记录数
     */
    @Test
    void shouldReturnPageResultWhenQuery() {
        // 构建分页查询 DTO 参数
        ActivityPlanPageQueryDTO dto = new ActivityPlanPageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);
        // 模拟缓存未命中的场景
        when(redisService.get(contains(RedisConstant.ACTIVITY_LIST_PREFIX))).thenReturn(null);
        // 准备模拟的数据库分页查询结果
        Page<ActivityPlan> page = new Page<>(1, 10);
        page.setTotal(4);
        page.setRecords(List.of(new ActivityPlan(), new ActivityPlan()));
        when(activityPlanMapper.selectPage(any(Page.class), any())).thenReturn(page);
        // 执行分页查询
        PageResult result = activityPlanService.pageQuery(dto);
        // 验证返回结果的正确性
        assertEquals(4, result.getTotal());
        assertEquals(2, result.getRecords().size());
    }

    /**
     * 验证变更状态时方案不存在会抛出异常
     */
    @Test
    void shouldThrowWhenChangeStatusPlanNotFound() {
        when(activityPlanMapper.selectById(1L)).thenReturn(null);

        assertThrows(BaseException.class, () -> activityPlanService.changeStatus(1L, 1));
    }

    /**
     * 验证仪表盘统计数据汇总结果
     */
    @Test
    void shouldReturnDashboardStats() {
        when(activityPlanMapper.selectCount(isNull())).thenReturn(5L);
        when(employeeMapper.selectCount(isNull())).thenReturn(3L);
        when(activityPlanMapper.selectCount(argThat(wrapper -> wrapper != null))).thenReturn(2L);

        Map<String, Object> stats = activityPlanService.getDashboardStats();

        assertEquals(5L, stats.get("totalPlans"));
        assertEquals(3L, stats.get("totalEmployees"));
        assertEquals(2L, stats.get("monthlyNewPlans"));
    }
}
