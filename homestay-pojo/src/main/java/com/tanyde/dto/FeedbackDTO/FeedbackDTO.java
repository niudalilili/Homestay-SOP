package com.tanyde.dto.FeedbackDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 反馈DTO
 *
 * @author Claude
 * @date 2026/3/4
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long recordId;

    private Long activityId;

    private Integer rating;

    private String detail;

    private String content;

    private Integer status;

}
