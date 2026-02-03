package com.tanyde.dto;


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

    private Integer page;

    private Integer pageSize;
}
