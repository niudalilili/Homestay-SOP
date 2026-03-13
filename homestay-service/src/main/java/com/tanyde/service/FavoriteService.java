package com.tanyde.service;

import com.tanyde.dto.FavoriteDTO.FavoriteDTO;
import com.tanyde.dto.FavoriteDTO.FavoritePageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.vo.FavoriteAdminVO;

/**
 * 收藏服务接口
 *
 * @author yh
 * @date 2026/3/4
 **/
public interface FavoriteService {

    /**
     * 分页查询收藏方案
     *
     * @param favoritePageQueryDTO 查询参数
     * @return 分页结果
     * @date 2026/3/4
     **/
    PageResult page(FavoritePageQueryDTO favoritePageQueryDTO);

    /**
     * 添加收藏方案
     *
     * @param favoriteDTO 收藏信息
     * @date 2026/3/4
     **/
    void add(FavoriteDTO favoriteDTO);

    /**
     * 取消收藏
     *
     * @param activityId 方案ID
     * @date 2026/3/4
     **/
    void cancel(Long activityId);

    /**
     * 检查收藏方案状态
     *
     * @param activityId 方案ID
     * @return 是否已收藏
     * @date 2026/3/4
     **/
    Boolean check(Long activityId);
    /**
     * 管理员分页查询收藏方案
     *
     * @param dto 查询参数
     * @return 分页结果
     * @date 2026/3/4
     **/
    PageResult adminPage(FavoritePageQueryDTO dto);
    /**
     * 管理员查询收藏方案详情
     *
     * @param id 收藏方案ID
     * @return 收藏方案详情
     * @date 2026/3/4
     **/
    FavoriteAdminVO adminDetail(Long id);
}
