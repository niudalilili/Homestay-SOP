package com.tanyde.dto.ActivityDTO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "活动计划DTO")
public class ActivityPlanDTO implements Serializable {
    private static final long serialVersionUID = 1L; // 修复序列化ID缺失

    private Long id;  // 添加这个字段，用于更新操作
    @Schema(description = "计划名称")
    private String planName;
    @Schema(description = "最小年龄")
    private Integer minAge;
    @Schema(description = "最大年龄")
    private Integer maxAge;
    @Schema(description = "活动类别")
    private String activityCategory;
    @Schema(description = "封面图片URL")
    private String coverUrl;
    @Schema(description = "季节")
    private Integer season;
    @Schema(description = "持续时间")
    private Integer duration;
    @Schema(description = "场景")
    private Integer scene;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "内容")
    private ActivityPlanContentDTO content;
    @Schema(description = "步骤")
    private List<ActivityStepDTO> steps;

}
