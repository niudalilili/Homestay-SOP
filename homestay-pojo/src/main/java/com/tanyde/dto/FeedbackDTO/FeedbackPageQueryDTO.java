package com.tanyde.dto.FeedbackDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 反馈分页查询DTO
 *
 * @author Claude
 * @date 2026/3/4
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackPageQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer page;

    private Integer pageSize;

    private Long recordId;

    private Long activityId;

    private Integer status;

}
