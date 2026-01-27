package com.tanyde.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPlan implements Serializable {
    private static final long serialVersionUID=1L;

    private Long id;

    private String planName;

    private String ageGroup;

    private String activityCategory;

    private Integer season;

    private Integer duration;

    private Integer scene;

    private Integer status;

    private LocalDateTime createTime;

    private  LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
