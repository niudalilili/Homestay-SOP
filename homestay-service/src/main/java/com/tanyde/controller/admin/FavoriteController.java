package com.tanyde.controller.admin;


import com.tanyde.dto.FavoriteDTO.FavoritePageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.FavoriteService;
import com.tanyde.vo.FavoriteAdminVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminFavorite")
@RequestMapping("/admin/favorite")
@Tag(name = "收藏管理", description = "收藏相关接口")
@Slf4j
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    /**
     * 分页查询收藏
     *
     * @param dto
     * @return com.tanyde.result.Result<com.tanyde.result.PageResult>
     * @date 2026/3/13 20:02
     **/
    @GetMapping("/list")
    public Result<PageResult> page(FavoritePageQueryDTO dto) {
        log.info("分页查询收藏: {}", dto);
        PageResult pageResult = favoriteService.adminPage(dto);
        return Result.success(pageResult);
    }

    /**
     * 查询收藏详情
     *
     * @param id
     * @return com.tanyde.result.Result<com.tanyde.vo.FavoriteAdminVO>
     * @date 2026/3/13 20:02
     **/
    @GetMapping("/{id}")
    public Result<FavoriteAdminVO> detail(@PathVariable Long id) {
        log.info("查询收藏详情: {}", id);
        return Result.success(favoriteService.adminDetail(id));
    }
}
