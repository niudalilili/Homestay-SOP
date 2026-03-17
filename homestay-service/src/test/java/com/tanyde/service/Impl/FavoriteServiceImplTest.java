package com.tanyde.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.dto.FavoriteDTO.FavoriteDTO;
import com.tanyde.dto.FavoriteDTO.FavoritePageQueryDTO;
import com.tanyde.entity.FavoritePO.Favorite;
import com.tanyde.exception.BaseException;
import com.tanyde.mapper.FavoriteMapper;
import com.tanyde.result.PageResult;
import com.tanyde.vo.FavoriteAdminVO;
import com.tanyde.vo.FavoritePlanVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * FavoriteServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteMapper favoriteMapper;
    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    /**
     * 验证当前用户收藏分页查询流程
     */
    @Test
    void shouldReturnPageForCurrentUser() {
        FavoritePageQueryDTO dto = new FavoritePageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);
        Page<FavoritePlanVO> page = new Page<>(1, 10);
        page.setTotal(2);
        page.setRecords(List.of(new FavoritePlanVO(), new FavoritePlanVO()));
        when(favoriteMapper.pageWithPlan(any(Page.class), any(FavoritePageQueryDTO.class))).thenReturn(page);

        try (MockedStatic<StpUtil> mocked = mockStatic(StpUtil.class)) {
            mocked.when(StpUtil::getLoginIdAsLong).thenReturn(9L);
            PageResult result = favoriteService.page(dto);
            assertEquals(2, result.getTotal());
            assertEquals(9L, dto.getUserId());
        }
    }

    /**
     * 验证重复收藏时抛出业务异常
     */
    @Test
    void shouldThrowWhenAddDuplicateFavorite() {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setActivityId(3L);
        when(favoriteMapper.selectByUserIdAndPlanId(7L, 3L)).thenReturn(new Favorite());

        try (MockedStatic<StpUtil> mocked = mockStatic(StpUtil.class)) {
            mocked.when(StpUtil::getLoginIdAsLong).thenReturn(7L);
            assertThrows(BaseException.class, () -> favoriteService.add(dto));
        }
    }

    /**
     * 验证管理员详情查询返回结果
     */
    @Test
    void shouldReturnDetailForAdmin() {
        FavoriteAdminVO vo = new FavoriteAdminVO();
        when(favoriteMapper.getDetailById(1L)).thenReturn(vo);

        FavoriteAdminVO result = favoriteService.adminDetail(1L);

        assertEquals(vo, result);
    }
}
