package com.tanyde.controller.user;


import com.tanyde.dto.ActivityDTO.ActivityPlanDTO;
import com.tanyde.dto.ActivityDTO.ActivityPlanPageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.ActivityPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 方案管理
 *
 **/
@RestController("userPlan")
@RequestMapping("/user/activityPlan")
@Tag(name = "方案管理", description = "方案相关接口")
@Slf4j
public class ActivityPlanController {

    @Autowired
    private ActivityPlanService activityPlanService;

    /**
     * 根据id查询方案
     **/
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询方案")
    public Result<ActivityPlanDTO> selectById(@PathVariable Long id) {
        ActivityPlanDTO activityPlanDTO = activityPlanService.selectById(id);
        log.info("查询到的方案为：{}", activityPlanDTO);
        return Result.success(activityPlanDTO);
    }

    /**
     * 分页查询活动方案
     *
     **/
    @GetMapping("/list")
    @Operation(summary = "分页查询活动方案")
    public Result<PageResult> page(@RequestBody ActivityPlanPageQueryDTO dto) {
        PageResult pageResult = activityPlanService.pageQuery(dto);
        log.info("分页查询活动方案：{}", pageResult);
        return Result.success(pageResult);
    }

    /**
     * 获得首页推荐的季节方案
     * @param season
     * @return
     * @date 2026/3/4 19:13
     **/
    @GetMapping("/recommend")
    @Operation(summary = "获得首页推荐季节方案")
    public Result<List<ActivityPlanDTO>>getRecommendPlan(@RequestBody Integer season,@RequestBody Integer limit){
        List<ActivityPlanDTO> list= activityPlanService.getRecommendPlan(season, limit);
        log.info("获得首页推荐季节方案：{}", list);
        return Result.success(list);
    }


}
