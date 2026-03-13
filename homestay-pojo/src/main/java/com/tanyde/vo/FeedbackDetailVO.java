package com.tanyde.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDetailVO implements Serializable {
    private Long id;
    private Long recordId;
    private Long activityId;
    private String planName;
    private Integer rating;
    private String detail;
    private LocalDateTime submissionTime;
    private Integer status;
}
