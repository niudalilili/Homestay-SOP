package com.tanyde.entity.ActivityPO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStep implements Serializable {

    private static final Long serialVersionUID=1L;

    private Long id;

    private Long activityPlanId;

    private Integer stepNumber;

    private String stepTitle;

    private Integer duration;

    private Integer stepType;

    private String knowledgeExplanation;

    private String actionDesc;

    private String guidanceSpeech;

    private String interactionTips;

    private String summary;

    private LocalDateTime createTime;

    private  LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
