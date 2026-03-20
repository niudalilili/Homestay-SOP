package com.tanyde.service.Impl;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.constant.RedisConstant;
import com.tanyde.dto.ActivityDTO.ActivityPlanContentDTO;
import com.tanyde.dto.ActivityDTO.ActivityPlanDTO;
import com.tanyde.dto.ActivityDTO.ActivityPlanPageQueryDTO;
import com.tanyde.dto.ActivityDTO.ActivityStepDTO;
import com.tanyde.entity.ActivityPO.ActivityPlan;
import com.tanyde.entity.ActivityPO.ActivityPlanContent;
import com.tanyde.entity.ActivityPO.ActivityStep;
import com.tanyde.mapper.ActivityPlanContentMapper;
import com.tanyde.mapper.ActivityPlanMapper;
import com.tanyde.mapper.ActivityStepMapper;
import com.tanyde.mapper.EmployeeMapper;
import com.tanyde.result.PageResult;
import com.tanyde.enumeration.ActivityStatus;
import com.tanyde.exception.BaseException;
import com.tanyde.service.RedisService;
import com.tanyde.utils.BatchCopyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import com.tanyde.service.ActivityPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityPlanServiceImpl implements ActivityPlanService {

    private final ActivityPlanMapper activityPlanMapper;
    private final ActivityPlanContentMapper activityPlanContentMapper;
    private final ActivityStepMapper activityStepMapper;
    private final EmployeeMapper employeeMapper;
    private final RedisService redisService;

    /**
     * 保存活动方案
     *
     * @param activityPlanDTO
     * @return void
     * @author TanyDe
     * @create 2026/1/27
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ActivityPlanDTO activityPlanDTO) {
        // 1. 防御性检查
        if (activityPlanDTO.getContent() == null) {
            throw new BaseException("活动方案扩展内容不能为空");
        }
        if (CollectionUtils.isEmpty(activityPlanDTO.getSteps())) {
            throw new BaseException("活动步骤不能为空");
        }
        //2.活动主表
        ActivityPlan activityPlan = new ActivityPlan();
        //复制相同类
        BeanUtils.copyProperties(activityPlanDTO, activityPlan);
        // 设置状态
        activityPlan.setStatus(ActivityStatus.DRAFT.getCode());
        //写入活动方案主表
        activityPlanMapper.insert(activityPlan);

        //获得主表主键
        Long planId = activityPlan.getId();

        //3.1对1关联表
        ActivityPlanContent activityPlanContent = new ActivityPlanContent();
        BeanUtils.copyProperties(
                activityPlanDTO.getContent(), activityPlanContent);
        //设置关联主表外键
        activityPlanContent.setActivityPlanId(planId);
        //写入活动关联表
        activityPlanContentMapper.insert(activityPlanContent);

        //4. 1对多步骤表
        //批量复制
        if (activityPlanDTO.getSteps() != null && !activityPlanDTO.getSteps().isEmpty()) {
            //调用工具批量复制
            ArrayList<ActivityStep> activitySteps = BatchCopyUtil.copyStepDTO(activityPlanDTO, planId);
            //批量写入活动步骤表
            int row = activityStepMapper.batchInsert(activitySteps);
            if (row != activityPlanDTO.getSteps().size()) {
                throw new BaseException("步骤批量写入数量不一致");
            }
        }

        //清除原有缓存
        clearActivityPlanListCache();
    }

    /**
     * 根据ids删除活动方案
     *
     * @param ids
     * @return void
     * @author TanyDe
     * @create 2026/1/27
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BaseException("请选择要删除的方案");
        }

        // 删除前检查状态，禁止删除已上线活动
        List<ActivityPlan> plans = activityPlanMapper.selectBatchIds(ids);
        for (ActivityPlan plan : plans) {
            if (plan.getStatus().equals(ActivityStatus.ONLINE.getCode())) {
                //当name为null时，显示id
                String planName = plan.getPlanName() != null ? plan.getPlanName() : "ID:" + plan.getId();
                throw new BaseException("活动【" + planName + "】处于上线状态，无法删除");
            }
        }

        activityPlanMapper.deleteByIds(ids);
        //删联表
        activityPlanContentMapper.delete(new LambdaQueryWrapper<ActivityPlanContent>()
                .in(ActivityPlanContent::getActivityPlanId, ids));
        //删步骤表
        activityStepMapper.deleteByActivityIds(ids);

        //删除id对应的活动详情缓存
        for (Long id : ids) {
            redisService.delete(RedisConstant.ACTIVITY_DETAIL_PREFIX + id);
        }
        //删除活动列表所有的缓存
        clearActivityPlanListCache();
    }

    /**
     * 根据id查询活动方案
     *
     * @param id
     * @return com.tanyde.dto.ActivityDTO.ActivityPlanDTO
     * @author TanyDe
     * @create 2026/1/31
     **/
    @Override
    public ActivityPlanDTO selectById(Long id) {
        //先从redis缓存中拿
        String cacheKey = RedisConstant.ACTIVITY_DETAIL_PREFIX + id;
        Object cacheValue = redisService.get(cacheKey);
        //判断类型是否符合，可以防止null情况
        if (cacheValue instanceof ActivityPlanDTO) {
            return (ActivityPlanDTO) cacheValue;
        }
        //从数据库查主表
        ActivityPlanDTO activityPlanDTO = new ActivityPlanDTO();
        ActivityPlan plan = activityPlanMapper.selectById(id);
        //验证活动方案是否存在
        if (plan == null) {
            throw new BaseException("活动方案不存在");
        }
        //查主表并赋值
        BeanUtils.copyProperties(plan, activityPlanDTO);
        //查关联表
        ActivityPlanContentDTO activityPlanContentDTO = new ActivityPlanContentDTO();
        ActivityPlanContent content = activityPlanContentMapper.selectOne(new LambdaQueryWrapper<ActivityPlanContent>()
                .eq(ActivityPlanContent::getActivityPlanId, id));
        BeanUtils.copyProperties(content, activityPlanContentDTO);
        //查步骤表
        List<ActivityStepDTO> activityStepDTOs = new ArrayList<>();
        List<ActivityStep> activitySteps = activityStepMapper.selectByPlanId(id);
        //格式转换
        for (ActivityStep activityStep : activitySteps) {
            ActivityStepDTO activityStepDTO = new ActivityStepDTO();
            BeanUtils.copyProperties(activityStep, activityStepDTO);
            activityStepDTOs.add(activityStepDTO);
        }
        //合并赋值
        activityPlanDTO.setContent(activityPlanContentDTO);
        activityPlanDTO.setSteps(activityStepDTOs);

        //将查到的数据缓存到redis中
        redisService.set(cacheKey, activityPlanDTO,
                RedisConstant.ACTIVITY_DETAIL_TTL, TimeUnit.SECONDS);

        return activityPlanDTO;
    }

    /**
     * 分页查询活动方案
     *
     * @param dto
     **/
    @Override
    public PageResult pageQuery(ActivityPlanPageQueryDTO dto) {
        //当分页参数为null时，设置默认值
        if (dto == null) {
            dto = new ActivityPlanPageQueryDTO();
        }
        Integer pageDto = dto.getPage() == null ? 1 : dto.getPage();
        Integer pageSize = dto.getPageSize() == null ? 10 : dto.getPageSize();
        //从缓存中获取
        String cacheKey = RedisConstant.ACTIVITY_LIST_PREFIX + buildListCacheKey(dto, pageDto, pageSize);
        Object cacheValue = redisService.get(cacheKey);
        if (cacheValue instanceof PageResult) {
            return (PageResult) cacheValue;
        }
        //数据库分页查询
        Page<ActivityPlan> page = new Page<>(pageDto, pageSize);
        LambdaQueryWrapper<ActivityPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(dto.getPlanName() != null && !dto.getPlanName().isEmpty(), ActivityPlan::getPlanName, dto.getPlanName())
                .eq(dto.getActivityCategory() != null && !dto.getActivityCategory().isEmpty(), ActivityPlan::getActivityCategory, dto.getActivityCategory())
                .eq(dto.getStatus() != null, ActivityPlan::getStatus, dto.getStatus())
                .eq(dto.getScene() != null, ActivityPlan::getScene, dto.getScene())
                .ge(dto.getMinAge() != null, ActivityPlan::getMinAge, dto.getMinAge())
                .le(dto.getMaxAge() != null, ActivityPlan::getMaxAge, dto.getMaxAge())
                .ge(dto.getMinDuration() != null, ActivityPlan::getDuration, dto.getMinDuration())
                .le(dto.getMaxDuration() != null, ActivityPlan::getDuration, dto.getMaxDuration())
                .orderByAsc(ActivityPlan::getCreateTime);
        if (dto.getSeason() != null) {
            queryWrapper.apply("(season & {0}) != 0", dto.getSeason());
        }
        IPage<ActivityPlan> resultPage = activityPlanMapper.selectPage(page, queryWrapper);
        PageResult pageResult = new PageResult(resultPage.getTotal(), resultPage.getRecords());
        //缓存到redis中
        redisService.set(cacheKey, pageResult, RedisConstant.ACTIVITY_LIST_TTL, TimeUnit.SECONDS);
        //返回pageResult
        return pageResult;
    }


    /**
     * 更新活动方案
     *
     * @param activityPlanDTO
     * @return
     * @date:
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ActivityPlanDTO activityPlanDTO) {
        // 检查ID是否为空
        if (activityPlanDTO.getId() == null) {
            throw new BaseException("活动方案ID不能为空");
        }
        //1.更新主表
        //复制
        ActivityPlan activityPlan = new ActivityPlan();
        BeanUtils.copyProperties(activityPlanDTO, activityPlan);
        //更新数据库
        int updatedRows = activityPlanMapper.updateById(activityPlan);
        if (updatedRows == 0) {
            throw new BaseException("活动方案不存在或更新失败");
        }

        //获得id
        Long planId = activityPlanDTO.getId();

        //2.更新关联表
        if (activityPlanDTO.getContent() != null) {
            // 首先检查关联表是否存在，不存在则插入
            ActivityPlanContent existingContent = activityPlanContentMapper.selectOne(new LambdaQueryWrapper<ActivityPlanContent>()
                    .eq(ActivityPlanContent::getActivityPlanId, planId));

            ActivityPlanContent activityPlanContent = new ActivityPlanContent();
            BeanUtils.copyProperties(activityPlanDTO.getContent(), activityPlanContent);
            //设置外键
            activityPlanContent.setActivityPlanId(planId);
            if (existingContent == null) {
                // 插入新记录
                activityPlanContentMapper.insert(activityPlanContent);
            } else {
                // 更新现有记录
                activityPlanContent.setId(existingContent.getId());
                activityPlanContentMapper.updateById(activityPlanContent);
            }
        }

        // 3. 更新步骤表（先删除再插入）
        //仅当 steps 不为 null 时才执行全量替换逻辑，防止误删步骤
        if (activityPlanDTO.getSteps() != null) {
            List<Long> ids = new ArrayList<>();
            ids.add(planId);
            // 删除原有步骤
            activityStepMapper.deleteByActivityIds(ids);

            //批量复制
            if (activityPlanDTO.getSteps() != null && !activityPlanDTO.getSteps().isEmpty()) {
                //调用工具批量复制
                ArrayList<ActivityStep> activitySteps = BatchCopyUtil.copyStepDTO(activityPlanDTO, planId);
                //批量写入活动步骤表
                int rows = activityStepMapper.batchInsert(activitySteps);
                //检查批量插入数量是否与steps数量一样
                if (rows != activitySteps.size()) {
                    throw new BaseException("步骤批量写入数量不一致");
                }
            }
        }
        //清除对应缓存
        redisService.delete(RedisConstant.ACTIVITY_DETAIL_PREFIX + planId);
        clearActivityPlanListCache();
    }

    /**
     * 修改状态
     *
     * @param id
     * @param status
     * @return
     * @date:
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeStatus(Long id, Integer status) {
        //检查ID是否为空
        if (id == null) {
            throw new BaseException("活动方案ID不能为空");
        }
        //检查状态是否为空
        ActivityPlan plan = activityPlanMapper.selectById(id);
        if (plan == null) {
            throw new BaseException("活动方案不存在");
        }
        //更新状态
        ActivityPlan update = new ActivityPlan();
        update.setId(id);
        update.setStatus(status);
        int rows = activityPlanMapper.updateById(update);
        if (rows == 0) {
            throw new BaseException("状态更新失败");
        }
        //清除对应缓存
        redisService.delete(RedisConstant.ACTIVITY_DETAIL_PREFIX + id);
        clearActivityPlanListCache();
    }

    /**
     * 仪表盘统计数据
     */
    @Override
    public Map<String, Object> getDashboardStats() {
        Long totalPlans = activityPlanMapper.selectCount(null);
        Long totalEmployees = employeeMapper.selectCount(null);
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.withDayOfMonth(1);
        LocalDateTime startTime = firstDay.atStartOfDay();
        LocalDateTime endTime = firstDay.plusMonths(1).atStartOfDay();
        Long monthlyNewPlans = activityPlanMapper.selectCount(new LambdaQueryWrapper<ActivityPlan>()
                .ge(ActivityPlan::getCreateTime, startTime)
                .lt(ActivityPlan::getCreateTime, endTime));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPlans", totalPlans == null ? 0L : totalPlans);
        stats.put("totalEmployees", totalEmployees == null ? 0L : totalEmployees);
        stats.put("monthlyNewPlans", monthlyNewPlans == null ? 0L : monthlyNewPlans);
        return stats;
    }

    /**
     * 获取推荐季节方案
     *
     * @param season
     * @return
     */
    @Override
    public List<ActivityPlanDTO> getRecommendPlan(Integer season, Integer limit) {
        List<ActivityPlan> activityPlans = activityPlanMapper.selectList(new LambdaQueryWrapper<ActivityPlan>()
                .apply("(season & {0}) != 0", season));
        List<ActivityPlanDTO> activityPlanDTOs = activityPlans.stream()
                .map(activityPlan -> {
                    ActivityPlanDTO activityPlanDTO = new ActivityPlanDTO();
                    BeanUtils.copyProperties(activityPlan, activityPlanDTO);
                    return activityPlanDTO;
                })
                .limit(limit)
                .collect(Collectors.toList());
        return activityPlanDTOs;
    }

    /**
     * 清空活动方案列表缓存
     *
     * @return
     * @date:
     **/
    private void clearActivityPlanListCache() {
        redisService.deleteByPattern(RedisConstant.ACTIVITY_LIST_PREFIX + "*");
    }

    /**
     * 构建活动方案列表缓存的键
     **/
    private String buildListCacheKey(ActivityPlanPageQueryDTO dto, Integer page, Integer pageSize) {
        StringBuilder builder = new StringBuilder();
        builder.append(formatKeyPart(dto.getPlanName())).append("|")
                .append(formatKeyPart(dto.getStatus())).append("|")
                .append(formatKeyPart(dto.getActivityCategory())).append("|")
                .append(formatKeyPart(dto.getSeason())).append("|")
                .append(formatKeyPart(dto.getScene())).append("|")
                .append(formatKeyPart(dto.getMinAge())).append("|")
                .append(formatKeyPart(dto.getMaxAge())).append("|")
                .append(formatKeyPart(dto.getMinDuration())).append("|")
                .append(formatKeyPart(dto.getMaxDuration())).append("|")
                .append(page).append("|")
                .append(pageSize);
        return builder.toString();
    }

    private String formatKeyPart(Object value) {
        if (value == null) {
            return "-";
        }
        String text = String.valueOf(value).trim();
        if (text.isEmpty()) {
            return "-";
        }
        return text.replace("|", "_");
    }


}
