package com.tanyde.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteAdminVO implements Serializable {
    private Long favoriteId;
    private Long userId;
    private Long activityId;
    private String planName;
    private Integer minAge;
    private Integer maxAge;
    private String activityCategory;
    private Integer season;
    private Integer duration;
    private Integer scene;
    private Integer status;
    private Integer stepCount;
    private String favoriteTime;
}