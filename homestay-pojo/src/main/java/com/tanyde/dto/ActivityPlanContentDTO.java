package com.tanyde.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPlanContentDTO implements Serializable {

    private String knowledgeGoal;

    private String emotionGoal;

    private String materials;

    private String spaceRequirement;

    private Integer preparationTime;

    private String staffTips;

    private String generalWarning;

    private String specialReminder;




}
