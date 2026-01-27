package com.tanyde.mapper;


import com.tanyde.annotation.AutoFill;
import com.tanyde.entity.ActivityPlan;
import com.tanyde.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
}
