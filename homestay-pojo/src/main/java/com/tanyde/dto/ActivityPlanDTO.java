package com.tanyde.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPlanDTO implements Serializable {
    private static final long serialVersionUID = 1L; // 修复序列化ID缺失

    private Long id;  // 添加这个字段，用于更新操作

    private String planName;

    private String ageGroup;

    private String activityCategory;

    private Integer season;

    private Integer duration;

    private Integer scene;

    private ActivityPlanContentDTO content;

    private List<ActivityStepDTO> steps;

}
