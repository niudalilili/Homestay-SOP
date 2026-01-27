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
public class ActivityPlanContent implements Serializable {

    private static final Long serialVersionUID=1L;

    private Long id;

    private Long activityPlanId;

    private String knowledgeGoal;

    private String emotionGoal;

    private String materials;

    private String spaceRequirement;

    private Integer preparationTime;

    private String staffTips;

    private String generalWarning;

    private String spacialReminder;

    private LocalDateTime createTime;

    private  LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
