package com.tanyde.controller.admin;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminFavorite")
@RequestMapping("/admin/favorite")
@Tag(name = "收藏管理", description = "收藏相关接口")
@Slf4j
public class FavoriteController {
}
