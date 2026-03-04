package com.tanyde.mapper;

import com.github.pagehelper.Page;
import com.tanyde.dto.FeedbackDTO.FeedbackPageQueryDTO;
import com.tanyde.entity.FeedbackPO.Feedback;
import org.apache.ibatis.annotations.Mapper;

/**
 * 反馈Mapper
 */
@Mapper
public interface FeedbackMapper {
    /**
     * 分页查询反馈
     *
     * @param dto 查询参数
     * @return 反馈分页结果
     */
    Page<Feedback> page(FeedbackPageQueryDTO dto);

    /**
     * 新增反馈
     *
     * @param feedback 反馈信息
     */
    void insert(Feedback feedback);
}
