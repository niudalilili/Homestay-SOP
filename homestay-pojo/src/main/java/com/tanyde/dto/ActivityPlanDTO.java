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

    private String planName;

    private String ageGroup;

    private String activityCategory;

    private Integer season;

    private Integer duration;

    private Integer scene;

    private ActivityPlanContentDTO content;

    private ArrayList<ActivityStepDTO> steps;

}
