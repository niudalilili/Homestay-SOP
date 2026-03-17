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
public class FavoritePlanVO implements Serializable {
    private Long id;
    private String planName;
    private String coverUrl;
    private Integer minAge;
    private Integer maxAge;
    private String activityCategory;
    private Integer season;
    private Integer duration;
    private Integer scene;
    private Integer status;
    private Integer stepCount;
}
