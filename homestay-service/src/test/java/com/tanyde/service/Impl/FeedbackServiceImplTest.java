package com.tanyde.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.dto.FeedbackDTO.FeedbackDTO;
import com.tanyde.dto.FeedbackDTO.FeedbackPageQueryDTO;
import com.tanyde.entity.FeedbackPO.Feedback;
import com.tanyde.exception.BaseException;
import com.tanyde.mapper.FeedbackMapper;
import com.tanyde.result.PageResult;
import com.tanyde.vo.FeedbackDetailVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * FeedbackServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock
    private FeedbackMapper feedbackMapper;
    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    /**
     * 验证提交反馈时会设置默认状态与提交时间
     */
    @Test
    void shouldSetDefaultStatusWhenSubmit() {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setDetail("d1");

        feedbackService.submit(dto);

        ArgumentCaptor<Feedback> captor = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackMapper).insert(captor.capture());
        assertEquals(0, captor.getValue().getStatus());
        assertNotNull(captor.getValue().getSubmissionTime());
    }

    /**
     * 验证分页查询返回总数与记录数
     */
    @Test
    void shouldReturnPageResultWhenPage() {
        FeedbackPageQueryDTO dto = new FeedbackPageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(2);
        Page<Feedback> page = new Page<>(1, 2);
        page.setTotal(3);
        page.setRecords(List.of(new Feedback(), new Feedback()));
        when(feedbackMapper.page(any(Page.class), any(FeedbackPageQueryDTO.class))).thenReturn(page);

        PageResult result = feedbackService.page(dto);

        assertEquals(3, result.getTotal());
        assertEquals(2, result.getRecords().size());
    }

    /**
     * 验证详情不存在时抛出业务异常
     */
    @Test
    void shouldThrowWhenDetailNotFound() {
        when(feedbackMapper.getDetailById(1L)).thenReturn(null);

        assertThrows(BaseException.class, () -> feedbackService.detailById(1L));
    }
}
