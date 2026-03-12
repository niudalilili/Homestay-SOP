package com.tanyde.controller.admin;


import com.tanyde.dto.FeedbackDTO.FeedbackPageQueryDTO;
import com.tanyde.result.PageResult;
import com.tanyde.result.Result;
import com.tanyde.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminFeedback")
@RequestMapping("/admin/feedback")
@Tag(name = "反馈管理", description = "反馈相关接口")
@Slf4j
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    /**
     * 分页查询反馈记录
     *
     * @param dto
     * @return com.tanyde.result.Result<com.tanyde.result.PageResult>
     * @date 2026/3/8 23:04
     **/
    @GetMapping("/list")
    public Result<PageResult> page(FeedbackPageQueryDTO dto) {
        // 记录请求日志
        log.info("分页查询反馈: {}", dto);
        // 调用服务获取分页结果
        PageResult pageResult = feedbackService.page(dto);
        // 返回统一结果
        return Result.success(pageResult);
    }





}
