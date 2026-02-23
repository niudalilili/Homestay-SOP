package com.tanyde.dto.ActivityDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPlanPageQueryDTO {
    private String planName;

    private Integer status;

    private String activityCategory;

    private Integer season;

    private Integer scene;

    private Integer minAge;

    private Integer maxAge;

    private Integer minDuration;

    private Integer maxDuration;

    private Integer page;

    private Integer pageSize;
}
