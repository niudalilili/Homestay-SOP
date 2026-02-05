package com.tanyde.mapper;

import com.tanyde.annotation.AutoFill;
import com.tanyde.entity.ActivityPlan;
import com.tanyde.entity.ActivityStep;
import com.tanyde.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ActivityStepMapper {

    /**
     * 批量插入活动步骤数据
     *
     * @param activitySteps
     * @return void
     * @author TanyDe
     * @create 2026/1/27
     **/
    int batchInsert(@Param("steps")ArrayList<ActivityStep> activitySteps);

    /**
     * 根据活动id删除活动步骤数据
     *
     * @param ids
     * @return void
     * @author TanyDe
     * @create 2026/1/31
     **/
    void deleteByActivityIds(List<Long> ids);

    /**
     * 根据planid查询
     *
     * @param id
     * @return
     **/
    List<ActivityStep> selectByPlanId(Long id);


}
