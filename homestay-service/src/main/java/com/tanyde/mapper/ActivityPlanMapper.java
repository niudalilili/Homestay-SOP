package com.tanyde.mapper;


import com.tanyde.annotation.AutoFill;
import com.tanyde.entity.ActivityPlan;
import com.tanyde.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
}
