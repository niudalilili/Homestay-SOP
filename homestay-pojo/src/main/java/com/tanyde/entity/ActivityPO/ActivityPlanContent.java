package com.tanyde.entity.ActivityPO;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("activity_plan_content")
public class ActivityPlanContent implements Serializable {

    private static final Long serialVersionUID=1L;

    private Long id;

    private Long activityPlanId;

    private String knowledgeGoal;

    private String emotionGoal;

    private String materials;

    private String spaceRequirement;

    private Integer preparationTime;

    private String staffTips;

    private String generalWarning;

    private String specialReminder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private  LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
