package com.tanyde.controller.user;

import com.tanyde.dto.FeedbackDTO.FeedbackDTO;
import com.tanyde.dto.FeedbackDTO.FeedbackPageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 反馈管理控制器
 */
@RestController
@RequestMapping("/user/feedback")
@Tag(name = "反馈管理", description = "反馈相关接口")
@Slf4j
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 提交反馈
     *
     * @param feedbackDTO 反馈参数
     * @return 结果
     */
    @PostMapping
    public Result submit(@RequestBody FeedbackDTO feedbackDTO) {
        // 记录请求日志
        log.info("提交反馈: {}", feedbackDTO);
        // 调用服务提交反馈
        feedbackService.submit(feedbackDTO);
        // 返回统一结果
        return Result.success();
    }

    /**
     * 分页查询反馈
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResult> page(FeedbackPageQueryDTO dto) {
        // 记录请求日志
        log.info("分页查询反馈: {}", dto);
        // 调用服务获取分页结果
        PageResult pageResult = feedbackService.page(dto);
        // 返回统一结果
        return Result.success(pageResult);
    }
}
