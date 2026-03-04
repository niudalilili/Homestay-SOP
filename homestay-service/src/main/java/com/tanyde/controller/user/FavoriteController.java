package com.tanyde.controller.user;

import com.tanyde.dto.FavoriteDTO.FavoriteDTO;
import com.tanyde.dto.FavoriteDTO.FavoritePageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.FavoriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏管理控制器
 *
 * @author yh
 * @date 2026/3/4
 **/
@RestController
@RequestMapping("/user/favorite")
@Tag(name = "收藏管理", description = "收藏相关接口")
@Slf4j
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    /**
     * 分页查询收藏方案
     *
     * @param dto 查询参数
     * @return 分页结果
     * @date 2026/3/4 19:13
     **/
    @GetMapping("/list")
    public Result<PageResult> page(FavoritePageQueryDTO dto) {
        log.info("分页查询收藏方案: {}", dto);
        PageResult pageResult = favoriteService.page(dto);
        return Result.success(pageResult);
    }

    /**
     * 添加收藏方案
     *
     * @param favoriteDTO 收藏信息
     * @return 结果
     * @date 2026/3/4 19:57
     **/
    @PostMapping("/add")
    public Result add(@RequestBody FavoriteDTO favoriteDTO) {
        log.info("添加收藏方案: {}", favoriteDTO);
        favoriteService.add(favoriteDTO);
        return Result.success();
    }

    /**
     * 取消收藏
     *
     * @param activityId 方案ID
     * @return 结果
     * @date 2026/3/4 19:57
     **/
    @DeleteMapping("/cancel/{activityId}")
    public Result cancel(@PathVariable Long activityId) {
        log.info("取消收藏方案, activityId: {}", activityId);
        favoriteService.cancel(activityId);
        return Result.success();
    }

    /**
     * 检查收藏方案状态
     *
     * @param activityId 方案ID
     * @return 是否已收藏
     * @date 2026/3/4 19:57
     **/
    @GetMapping("/check/{activityId}")
    public Result<Boolean> check(@PathVariable Long activityId) {
        log.info("检查收藏方案状态, activityId: {}", activityId);
        Boolean aBoolean = favoriteService.check(activityId);
        return Result.success(aBoolean);
    }
}
