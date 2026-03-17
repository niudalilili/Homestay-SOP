package com.tanyde.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.tanyde.dto.ActivityDTO.ActivityPlanDTO;
import com.tanyde.dto.ActivityDTO.ActivityStepDTO;
import lombok.extern.slf4j.Slf4j;
import com.tanyde.entity.ActivityPO.ActivityStep;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;



/**
 * 活动方案批量复制并设置user和时间
 **/
@Slf4j
public final class BatchCopyUtil {
    private BatchCopyUtil() {}


   /**
    *  提取activityPlanDTO复制到activityStep
    *
    * @param dto
    * @return
    * @date 2026/3/16 15:46
    **/
    public static ArrayList<ActivityStep> copyStepDTO(ActivityPlanDTO dto,Long planId){
        ArrayList<ActivityStep> activitySteps = new ArrayList<>();
        for (ActivityStepDTO activityStepDTO : dto.getSteps()) {
            LocalDateTime nowTime = LocalDateTime.now();
            Long userId = StpUtil.getLoginIdAsLong();
            //设置关联主表外键和时间用户
            ActivityStep activityStep = ActivityStep.builder()
                    .activityPlanId(planId)
                    .createTime(nowTime).updateTime(nowTime).createUser(userId).updateUser(userId)
                    .build();
            //传递参数
            BeanUtils.copyProperties(activityStepDTO, activityStep);
            //加入List
            activitySteps.add(activityStep);
        }

        return activitySteps;
    }


}
