package com.tanyde.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.dto.FeedbackDTO.FeedbackDTO;
import com.tanyde.dto.FeedbackDTO.FeedbackPageQueryDTO;
import com.tanyde.entity.FeedbackPO.Feedback;
import com.tanyde.exception.BaseException;
import com.tanyde.mapper.FeedbackMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.FeedbackService;
import com.tanyde.vo.FeedbackDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 反馈服务实现
 */
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackMapper feedbackMapper;

    /**
     * 提交反馈
     *
     * @param feedbackDTO 反馈参数
     */
    @Override
    public void submit(FeedbackDTO feedbackDTO) {
        Integer rating = feedbackDTO.getRating() == null ? 5 : feedbackDTO.getRating();
        if (rating < 1 || rating > 5) {
            throw new BaseException("评分星级需在1-5之间");
        }
        String detail = StringUtils.hasText(feedbackDTO.getDetail()) ? feedbackDTO.getDetail() : feedbackDTO.getContent();
        if (!StringUtils.hasText(detail)) {
            throw new BaseException("反馈内容不能为空");
        }
        // 构建反馈实体
        Feedback feedback = new Feedback();
        // 拷贝参数
        BeanUtils.copyProperties(feedbackDTO, feedback);
        Long recordId = feedbackDTO.getRecordId();
        if (recordId == null) {
            recordId = feedbackDTO.getActivityId();
        }
        if (recordId == null) {
            recordId = 0L;
        }
        feedback.setRecordId(recordId);
        feedback.setRating(rating);
        feedback.setDetail(detail);
        // 设置提交时间
        feedback.setSubmissionTime(LocalDateTime.now());
        // 默认状态
        if (feedback.getStatus() == null) {
            feedback.setStatus(0);
        }
        // 写入数据库
        feedbackMapper.insert(feedback);
    }

    /**
     * 分页查询反馈
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    @Override
    public PageResult page(FeedbackPageQueryDTO dto) {
        // 处理默认分页参数
        Integer page = dto.getPage() == null ? 1 : dto.getPage();
        Integer pageSize = dto.getPageSize() == null ? 20 : dto.getPageSize();
        Page<Feedback> pageQuery = new Page<>(page, pageSize);
        IPage<Feedback> result = feedbackMapper.page(pageQuery, dto);
        return new PageResult(result.getTotal(), result.getRecords());
    }

    /**
     * 查询反馈详情
     *
     * @param id 反馈ID
     * @return 反馈详情
     */
    @Override
    public FeedbackDetailVO detailById(Long id) {
        FeedbackDetailVO detail =feedbackMapper.getDetailById(id);
        if(detail == null){
            throw new BaseException("反馈不存在");
        }
        return detail;
    }
}
