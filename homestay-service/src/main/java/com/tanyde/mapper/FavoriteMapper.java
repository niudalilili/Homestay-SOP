package com.tanyde.mapper;

import com.github.pagehelper.Page;
import com.tanyde.dto.FavoriteDTO.FavoritePageQueryDTO;
import com.tanyde.entity.FavoritePO.Favorite;
import com.tanyde.vo.FavoritePlanVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 收藏Mapper
 *
 * @author yh
 * @date 2026/3/4
 **/
@Mapper
public interface FavoriteMapper {

    /**
     * 分页查询收藏方案
     *
     * @param dto 查询参数
     * @return 收藏列表
     */
    Page<Favorite> page(FavoritePageQueryDTO dto);

    Page<FavoritePlanVO> pageWithPlan(FavoritePageQueryDTO dto);

    /**
     * 添加收藏
     *
     * @param favorite 收藏信息
     */
    void insert(Favorite favorite);

    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param activityId 方案ID
     */
    void delete(@Param("userId") Long userId, @Param("activityId") Long activityId);

    /**
     * 根据用户ID和方案ID查询收藏
     *
     * @param userId 用户ID
     * @param activityId 方案ID
     * @return 收藏信息
     */
    Favorite selectByUserIdAndPlanId(@Param("userId") Long userId,
                                     @Param("activityId") Long activityId);
}
