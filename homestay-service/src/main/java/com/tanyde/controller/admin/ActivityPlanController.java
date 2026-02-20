package com.tanyde.controller.admin;

import com.tanyde.dto.ActivityPlanDTO;
import com.tanyde.dto.ActivityPlanPageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.ActivityPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 方案管理
 *
 * @author TanyDe
 * @create 2026/1/27
 **/
@RestController
@RequestMapping("/admin/activityPlan")
@Tag(name = "方案管理", description = "用户相关接口")
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
    @Operation(summary = "新增活动方案")
    public Result save(@RequestBody ActivityPlanDTO activityPlanDTO) {
        log.info("新增活动方案:{}", activityPlanDTO);
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
    @Operation(summary = "根据ids删除活动方案")
    public Result deleteById(@RequestBody List<Long> ids) {
        log.info("根据id删除活动方案:{}", ids);
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
    @Operation(summary = "根据id查询活动方案")
    public Result<ActivityPlanDTO> selectById(@PathVariable Long id) {
        ActivityPlanDTO activityPlanDTO = activityPlanService.selectById(id);
        log.info("根据id:{}查询活动方案:{}", id, activityPlanDTO);
        return Result.success(activityPlanDTO);
    }

    /**
     * 分页查询活动方案
     *
     * @param activityPlanPageQueryDTO
     **/
    @GetMapping("/page")
    @Operation(summary = "分页查询活动方案")
    public Result<PageResult> page(ActivityPlanPageQueryDTO activityPlanPageQueryDTO) {
        PageResult pageResult = activityPlanService.pageQuery(activityPlanPageQueryDTO);
        log.info("分页查询活动方案:{}", pageResult);
        return Result.success(pageResult);
    }

    /**
     * 仪表盘统计数据
     *
     * @return 统计结果
     */
    @GetMapping("/stats")
    @Operation(summary = "仪表盘统计数据")
    public Result<Map<String, Object>> stats() {
        return Result.success(activityPlanService.getDashboardStats());
    }

    /**
     * 更新活动方案
     *
     * @param activityPlanDTO
     **/
    @PutMapping("/update")
    @Operation(summary = "更新活动方案")
    public Result update(@RequestBody ActivityPlanDTO activityPlanDTO) {
        activityPlanService.update(activityPlanDTO);
        log.info("更新活动方案:{}", activityPlanDTO);
        return Result.success();
    }

    /**
     * 修改状态
     *
     * @param status
     * @param id
     **/
    @PutMapping("/status/{status}")
    @Operation(summary = "更新活动方案状态")
    public Result changeStatus(@PathVariable Integer status, @RequestParam Long id) {
        activityPlanService.changeStatus(id, status);
        return Result.success();
    }

}
