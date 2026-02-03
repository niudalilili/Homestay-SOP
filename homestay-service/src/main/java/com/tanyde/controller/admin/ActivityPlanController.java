package com.tanyde.controller.admin;

import com.tanyde.dto.ActivityPlanDTO;
import com.tanyde.dto.ActivityPlanPageQueryDTO;
import com.tanyde.dto.EmployeeDTO;
import com.tanyde.entity.ActivityPlan;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.ActivityPlanService;
import com.tanyde.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 方案管理
 * @author TanyDe
 * @create 2026/1/27
 **/
@RestController
@RequestMapping("/admin/activityPlan")
@Slf4j
public class ActivityPlanController {

    @Autowired
    private ActivityPlanService activityPlanService;


    /**
     * 新增活动方案
     *
     * @param activityPlanDTO
     * @return com.tanyde.result.Result
     * @author TanyDe
     * @create 2026/1/27
     **/
    @PostMapping
    @ApiOperation("新增活动方案")
    public Result save(@RequestBody ActivityPlanDTO activityPlanDTO){
        log.info("新增活动方案:{}",activityPlanDTO);
        activityPlanService.save(activityPlanDTO);
        return Result.success();
    }
    
    /**
     * 根据ids删除活动方案
     * 
     * @param ids
     * @return 
     * @author TanyDe
     * @create 2026/1/31
     **/
    @DeleteMapping("/batch")
    @ApiOperation("根据ids删除活动方案")
    public Result deletById(@RequestBody List<Long> ids){
        log.info("根据id删除活动方案:{}",ids);
        activityPlanService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 根据id查询活动方案
     *
     * @param id
     * @return
     * @author TanyDe
     * @create 2026/1/31
     **/
    @GetMapping("/{id}")
    @ApiOperation("根据id查询活动方案")
    public Result<ActivityPlanDTO> selectById(@PathVariable Long id){
        ActivityPlanDTO activityPlanDTO=activityPlanService.selectById(id);
        log.info("根据id:{}查询活动方案:{}",id,activityPlanDTO);
        return Result.success(activityPlanDTO);
    }

    /**
     * 分页查询活动方案
     *
     * @param activityPlanPageQueryDTO
     **/
    @GetMapping("/page")
    @ApiOperation("分页查询活动方案")
    public Result<PageResult> page(ActivityPlanPageQueryDTO activityPlanPageQueryDTO){
        PageResult pageResult=activityPlanService.pageQuery(activityPlanPageQueryDTO);
        log.info("分页查询活动方案:{}",pageResult);
        return Result.success(pageResult);
    }



}
