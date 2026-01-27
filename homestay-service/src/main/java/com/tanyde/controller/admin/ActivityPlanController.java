package com.tanyde.controller.admin;

import com.tanyde.dto.ActivityPlanDTO;
import com.tanyde.dto.EmployeeDTO;
import com.tanyde.result.Result;
import com.tanyde.service.ActivityPlanService;
import com.tanyde.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation("新增活动方案主表")
    public Result save(@RequestBody ActivityPlanDTO activityPlanDTO){
        log.info("新增活动方案:{}",activityPlanDTO);
        activityPlanService.save(activityPlanDTO);
        return Result.success();
    }



}
