package com.tanyde.entity.ActivityPO;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "活动方案")
@TableName("activity_plan")
public class ActivityPlan implements Serializable {
    private static final long serialVersionUID=1L;

    private Long id;
    @Schema(description = "方案名称")
    private String planName;
    @Schema(description = "最小年龄")
    private Integer minAge;
    @Schema(description = "最大年龄")
    private Integer maxAge;
    @Schema(description = "活动类别")
    private String activityCategory;
    @Schema(description = "封面图片")
    private  String coverUrl;
    @Schema(description = "季节")
    private Integer season;
    @Schema(description = "时长")
    private Integer duration;
    @Schema(description = "场景")
    private Integer scene;
    @Schema(description = "状态")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private  LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
