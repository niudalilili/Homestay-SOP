package com.tanyde.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.dto.FavoriteDTO.FavoriteDTO;
import com.tanyde.dto.FavoriteDTO.FavoritePageQueryDTO;
import com.tanyde.entity.FavoritePO.Favorite;
import com.tanyde.exception.BaseException;
import com.tanyde.mapper.FavoriteMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.FavoriteService;
import com.tanyde.vo.FavoriteAdminVO;
import com.tanyde.vo.FavoritePlanVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteMapper favoriteMapper;

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
        Page<FavoritePlanVO> page = new Page<>(pageDto, pageSizeDto);
        IPage<FavoritePlanVO> result = favoriteMapper.pageWithPlan(page, dto);
        return new PageResult(result.getTotal(), result.getRecords());
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

    /**
     * 管理员分页查询收藏方案
     *
     * @param dto 查询参数
     * @return 分页结果
     * @date 2026/3/4 20:29
     **/
    @Override
    public PageResult adminPage(FavoritePageQueryDTO dto) {
            //排除dto为null，设置默认值
            if (dto == null) {
                dto = new FavoritePageQueryDTO();}
            Integer pageDto = dto.getPage() == null ? 1 : dto.getPage();
            Integer pageSizeDto = dto.getPageSize() == null ? 10 : dto.getPageSize();
            Page<FavoriteAdminVO> page = new Page<>(pageDto, pageSizeDto);
            IPage<FavoriteAdminVO> result = favoriteMapper.pageAdmin(page, dto);
            return new PageResult(result.getTotal(), result.getRecords());
    }

    /**
     * 管理员查询收藏方案详情
     *
     * @param id 收藏方案ID
     * @return 收藏方案详情
     * @date 2026/3/4 20:29
     **/
    @Override
    public FavoriteAdminVO adminDetail(Long id) {
        FavoriteAdminVO detail = favoriteMapper.getDetailById(id);
        if (detail == null) {
            throw new BaseException("收藏不存在");
        }
        return detail;
    }
}
