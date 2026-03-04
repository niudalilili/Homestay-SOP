package com.tanyde.mapper;


import com.github.pagehelper.Page;
import com.tanyde.annotation.AutoFill;
import com.tanyde.dto.ActivityDTO.ActivityPlanDTO;
import com.tanyde.dto.ActivityDTO.ActivityPlanPageQueryDTO;
import com.tanyde.entity.ActivityPO.ActivityPlan;
import com.tanyde.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.time.LocalDateTime;

@Mapper
public interface ActivityPlanMapper {


    /**
     * 插入活动方案数据
     *
     * @param activityPlan
     * @return void
     * @author TanyDe
     * @create 2026/1/27
     **/
    @AutoFill(value = OperationType.INSERT)
    void insert(ActivityPlan activityPlan);

    /**
     * 根据ids删除活动方案
     *
     * @param ids
     * @return void
     * @create 2026/1/31
     **/
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询
     *
     * @param id
     * @return java.lang.Object
     **/
    ActivityPlan selectById(Long id);

    /**
     * 分页查询
     *
     * @param dto
     * @return
     **/
    Page<ActivityPlan> pageQuery(ActivityPlanPageQueryDTO dto);

    /**
     * 更新活动方案
     *
     * @param activityPlan
     * @return
     * @date:
     **/
    @AutoFill(value=OperationType.UPDATE)
    int update(ActivityPlan activityPlan);

    /**
     * 统计活动方案总数
     *
     * @return 总数量
     */
    Integer countAll();

    /**
     * 按创建时间范围统计活动方案数量
     *
     * @param startTime 起始时间（包含）
     * @param endTime 结束时间（不包含）
     * @return 数量
     */
    Integer countByCreateTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


    /**
     * 按季查询活动方案
     *
     * @param season 季
     * @return 活动方案
     */
    List<ActivityPlan> getBySeason(Integer season);
}
