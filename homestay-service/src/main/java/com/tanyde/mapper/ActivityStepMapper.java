package com.tanyde.mapper;

import com.tanyde.annotation.AutoFill;
import com.tanyde.entity.ActivityStep;
import com.tanyde.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

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
    @AutoFill(value= OperationType.INSERT)
    int batchInsert(@Param("steps")ArrayList<ActivityStep> activitySteps);
}
