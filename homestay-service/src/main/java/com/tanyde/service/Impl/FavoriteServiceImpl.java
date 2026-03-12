package com.tanyde.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tanyde.dto.FavoriteDTO.FavoriteDTO;
import com.tanyde.dto.FavoriteDTO.FavoritePageQueryDTO;
import com.tanyde.entity.FavoritePO.Favorite;
import com.tanyde.exception.BaseException;
import com.tanyde.mapper.FavoriteMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.FavoriteService;
import com.tanyde.vo.FavoritePlanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;

    /**
     * 分页查询收藏方案
     *
     * @param dto 查询参数
     * @return 分页结果
     * @date 2026/3/4 20:29
     **/
    @Override
    public PageResult page(FavoritePageQueryDTO dto) {
        // 获取当前用户ID
        dto.setUserId(StpUtil.getLoginIdAsLong());
        // 当分页参数为null时,设置默认值
        Integer pageDto = dto.getPage() == null ? 1 : dto.getPage();
        Integer pageSizeDto = dto.getPageSize() == null ? 10 : dto.getPageSize();
        // 设置分页参数
        PageHelper.startPage(pageDto, pageSizeDto);
        // 获得数据库数据
        Page<FavoritePlanVO> page = favoriteMapper.pageWithPlan(dto);
        // 返回分页结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加收藏方案
     *
     * @param favoriteDTO 收藏信息
     * @return void
     * @date 2026/3/4 20:29
     **/
    @Override
    public void add(FavoriteDTO favoriteDTO) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 查询是否已收藏
        Favorite exist = favoriteMapper.selectByUserIdAndPlanId(userId, favoriteDTO.getActivityId());
        if (exist != null) {
           throw new BaseException("方案已收藏");
        }
        // 构建收藏对象
        Favorite favorite = Favorite.builder()
                .userId(userId)
                .activityId(favoriteDTO.getActivityId())
                .favoriteTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        // 插入数据库
        favoriteMapper.insert(favorite);
    }

    /**
     * 取消收藏
     *
     * @param activityId 方案ID
     * @return void
     * @date 2026/3/4 20:29
     **/
    @Override
    public void cancel(Long activityId) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 删除收藏记录
        favoriteMapper.delete(userId, activityId);
    }

    /**
     * 检查收藏方案状态
     *
     * @param activityId 方案ID
     * @return Boolean 是否已收藏
     * @date 2026/3/4 20:29
     **/
    @Override
    public Boolean check(Long activityId) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 查询收藏记录
        return favoriteMapper.selectByUserIdAndPlanId(userId, activityId) != null;
    }
}
