package com.tanyde.entity.FavoritePO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "收藏方案")
public class Favorite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "收藏方案id")
    private Long id;
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "方案id")
    private Long activityId;
    @Schema(description = "收藏时间")
    private String favoriteTime;

}
