package com.tanyde.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.dto.FeedbackDTO.FeedbackPageQueryDTO;
import com.tanyde.entity.FeedbackPO.Feedback;
import com.tanyde.vo.FeedbackDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    IPage<Feedback> page(Page<Feedback> page,
                         @Param("dto") FeedbackPageQueryDTO dto);

    /**
     * 新增反馈
     *
     * @param feedback 反馈信息
     */
    void insert(Feedback feedback);

    /**
     * 根据id查询反馈详情
     *
     * @param id 反馈id
     * @return 反馈详情
     */
    FeedbackDetailVO getDetailById(Long id);
}
