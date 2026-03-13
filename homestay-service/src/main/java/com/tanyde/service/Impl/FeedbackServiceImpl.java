package com.tanyde.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tanyde.dto.FeedbackDTO.FeedbackDTO;
import com.tanyde.dto.FeedbackDTO.FeedbackPageQueryDTO;
import com.tanyde.entity.FeedbackPO.Feedback;
import com.tanyde.exception.BaseException;
import com.tanyde.mapper.FeedbackMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.FeedbackService;
import com.tanyde.vo.FeedbackDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 反馈服务实现
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;

    /**
     * 提交反馈
     *
     * @param feedbackDTO 反馈参数
     */
    @Override
    public void submit(FeedbackDTO feedbackDTO) {
        // 构建反馈实体
        Feedback feedback = new Feedback();
        // 拷贝参数
        BeanUtils.copyProperties(feedbackDTO, feedback);
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
        // 启动分页
        PageHelper.startPage(page, pageSize);
        // 执行查询
        Page<Feedback> result = feedbackMapper.page(dto);
        // 返回分页结果
        return new PageResult(result.getTotal(), result.getResult());
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
