package com.tanyde.mapper;


import com.tanyde.annotation.AutoFill;
import com.tanyde.dto.ActivityPlanContentDTO;
import com.tanyde.entity.ActivityPlanContent;
import com.tanyde.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityPlanContentMapper {

    /**
     * 插入活动计划关联数据
     *
     * @param activityPlanContent
     * @return void
     * @author TanyDe
     * @create 2026/1/27
     **/
    @AutoFill(value= OperationType.INSERT)
    void insert(ActivityPlanContent activityPlanContent);

    /**
     * 根据活动ids删除活动计划关联数据
     *
     * @param ids
     * @return void
     * @author TanyDe
     * @create 2026/1/31
     **/
    void deleteByActivityIds(List<Long> ids);

    /**
     * 根据plan_id查询
     *
     * @param id
     * @return
     **/
    ActivityPlanContent selectByPlanId(Long id);
}
