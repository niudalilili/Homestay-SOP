package com.tanyde.service.Impl;


import com.tanyde.context.BaseContext;
import com.tanyde.dto.ActivityPlanContentDTO;
import com.tanyde.dto.ActivityPlanDTO;
import com.tanyde.dto.ActivityStepDTO;
import com.tanyde.entity.ActivityPlan;
import com.tanyde.entity.ActivityPlanContent;
import com.tanyde.entity.ActivityStep;
import com.tanyde.mapper.ActivityPlanContentMapper;
import com.tanyde.mapper.ActivityPlanMapper;
import com.tanyde.mapper.ActivityStepMapper;
import com.tanyde.service.ActivityPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityPlanServiceImpl implements ActivityPlanService {
    @Autowired
    private ActivityPlanMapper activityPlanMapper;
    @Autowired
    private ActivityPlanContentMapper activityPlanContentMapper;
    @Autowired
    private ActivityStepMapper activityStepMapper;

    /**
     * 保存活动方案
     *
     * @param activityPlanDTO
     * @return void
     * @author TanyDe
     * @create 2026/1/27
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ActivityPlanDTO activityPlanDTO) {
        //活动主表
        ActivityPlan activityPlan=new ActivityPlan();
        //复制相同类
        BeanUtils.copyProperties(activityPlanDTO,activityPlan);
        //设置状态为草稿
        activityPlan.setStatus(1);
        //写入活动方案主表
        activityPlanMapper.insert(activityPlan);

        //获得主表主键
        Long planId=activityPlan.getId();

        //1对1关联表
        ActivityPlanContent activityPlanContent=new ActivityPlanContent();
        BeanUtils.copyProperties(
                activityPlanDTO.getContent(),activityPlanContent);
        //设置关联主表外键
        activityPlanContent.setActivityPlanId(planId);
        //写入活动关联表
        activityPlanContentMapper.insert(activityPlanContent);

        //1对多步骤表
        ArrayList<ActivityStep> activitySteps=new ArrayList<>();
        //批量复制
        for (ActivityStepDTO activityStepDTO : activityPlanDTO.getSteps()) {
            ActivityStep activityStep=new ActivityStep();
            BeanUtils.copyProperties(activityStepDTO,activityStep);
            //设置关联主表外键和时间用户
            activityStep.setActivityPlanId(planId);
            activityStep.setCreateTime(LocalDateTime.now());
            activityStep.setUpdateTime(LocalDateTime.now());
            activityStep.setCreateUser(BaseContext.getCurrentId());
            activityStep.setUpdateUser(BaseContext.getCurrentId());
            //加入List
            activitySteps.add(activityStep);
        }
        //批量写入活动步骤表
        int rows=activityStepMapper.batchInsert(activitySteps);
        //检查批量插入数量是否与steps数量一样
        if(rows!=activitySteps.size()){
            throw new  IllegalStateException("步骤批量写入数量不一致");
        }
    }

    /**
     * 根据ids删除活动方案
     *
     * @param ids
     * @return void
     * @author TanyDe
     * @create 2026/1/27
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> ids) {
        //删主表
        activityPlanMapper.deleteByIds(ids);
        //删联表
        activityPlanContentMapper.deleteByActivityIds(ids);
        //删步骤表
        activityStepMapper.deleteByActivityIds(ids);
    }

    /**
     * 根据id查询活动方案
     *
     * @param id
     * @return com.tanyde.dto.ActivityPlanDTO
     * @author TanyDe
     * @create 2026/1/31
     **/
    @Override
    public ActivityPlanDTO selectById(Long id) {
        ActivityPlanDTO activityPlanDTO=new ActivityPlanDTO();
        //查主表并赋值
        BeanUtils.copyProperties(activityPlanMapper.selectById(id),activityPlanDTO);
        //查关联表
        ActivityPlanContentDTO activityPlanContentDTO=new ActivityPlanContentDTO();
        BeanUtils.copyProperties(activityPlanContentMapper.selectByPlanId(id),activityPlanContentDTO);
        //查步骤表
        List<ActivityStepDTO> activityStepDTOs=new ArrayList<>();
        List<ActivityStep> activitySteps=activityStepMapper.selectByPlanId(id);
        //格式转换
        for (ActivityStep activityStep : activitySteps) {
            ActivityStepDTO activityStepDTO=new ActivityStepDTO();
            BeanUtils.copyProperties(activityStep,activityStepDTO);
            activityStepDTOs.add(activityStepDTO);
        }
        //合并赋值
        activityPlanDTO.setContent(activityPlanContentDTO);
        activityPlanDTO.setSteps(activityStepDTOs);

        return activityPlanDTO;
    }
}
