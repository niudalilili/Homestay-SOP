package com.tanyde.controller.user;


import com.tanyde.dto.ActivityDTO.ActivityPlanDTO;
import com.tanyde.dto.ActivityDTO.ActivityPlanPageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 方案管理
 *
 **/
@RestController("userPlan")
@RequestMapping("/user/activityPlan")
@Tag(name = "方案管理", description = "方案相关接口")
@Slf4j
public class ActivityPlanController {

    /**
     * 根据id查询方案
     **/
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询方案")
    public Result<ActivityPlanDTO> selectById(@PathVariable Integer id) {


        return Result.success();
    }

    /**
     * 分页查询活动方案
     *
     **/
    @GetMapping("/page")
    @Operation(summary = "分页查询活动方案")
    public Result<PageResult> page(@RequestBody ActivityPlanPageQueryDTO dto) {


        return Result.success();
    }



}
