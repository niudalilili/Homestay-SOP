package com.tanyde.service;

import com.tanyde.dto.FeedbackDTO.FeedbackDTO;
import com.tanyde.dto.FeedbackDTO.FeedbackPageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.vo.FeedbackDetailVO;

/**
 * 反馈服务接口
 */
public interface FeedbackService {
    /**
     * 提交反馈
     *
     * @param feedbackDTO 反馈参数
     */
    void submit(FeedbackDTO feedbackDTO);

    /**
     * 分页查询反馈
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    PageResult page(FeedbackPageQueryDTO dto);

    /**
     * 根据id查询反馈详情
     *
     * @param id
     * @return
     */
    FeedbackDetailVO detailById(Long id);
}
