package com.tanyde.mapper;


import com.tanyde.annotation.AutoFill;
import com.tanyde.entity.ActivityPlanContent;
import com.tanyde.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

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
}
