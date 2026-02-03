package com.tanyde.service;

import com.tanyde.dto.ActivityPlanDTO;
import com.tanyde.dto.ActivityPlanPageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.service.Impl.ActivityPlanServiceImpl;

import java.util.List;

public interface ActivityPlanService {


    /**
     * 保存活动方案
     *
    * @param activityPlanDTO
     * @return void
     * @author TanyDe
     * @create 2026/1/27
     **/
    void save(ActivityPlanDTO activityPlanDTO);

    /**
     * 根据ids删除活动方案
     *
    * @param ids
     * @return void
     * @author TanyDe
     * @create 2026/1/27
     **/
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询活动方案
     *
     * @param id
     * @return com.tanyde.dto.ActivityPlanDTO
     * @author TanyDe
     * @create 2026/1/31
     **/
    ActivityPlanDTO selectById(Long id);

    /**
     * 分页查询活动方案
     * @param dto
     **/
    PageResult pageQuery(ActivityPlanPageQueryDTO dto);
}
