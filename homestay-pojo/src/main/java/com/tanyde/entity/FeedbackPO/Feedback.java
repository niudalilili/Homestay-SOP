package com.tanyde.entity.FeedbackPO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 反馈实体类
 *
 * @author Claude
 * @date 2026/3/4
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "活动反馈")
public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "执行记录id")
    private Long recordId;

    @Schema(description = "方案id")
    private Long activityId;

    @Schema(description = "评分，1-5")
    private Integer rating;

    @Schema(description = "反馈内容")
    private String detail;

    @Schema(description = "提交时间")
    private LocalDateTime submissionTime;

    @Schema(description = "状态(0-待处理,1-已处理)")
    private Integer status;

}
