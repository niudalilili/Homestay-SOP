package com.tanyde.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStepDTO implements Serializable {
    private Integer stepNumber;

    private String stepTitle;

    private Integer duration;

    private Integer stepType;

    private String knowledgeExplanation;

    private String actionDesc;

    private String guidanceSpeech;

    private String interactionTips;

    private String summary;
}
