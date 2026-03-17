package com.tanyde.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanyde.constant.RedisConstant;
import com.tanyde.dto.LoginDTO.PermissionDTO;
import com.tanyde.dto.LoginDTO.PermissionPageQueryDTO;
import com.tanyde.dto.LoginDTO.RoleDTO;
import com.tanyde.entity.LoginPO.Permission;
import com.tanyde.entity.LoginPO.Role;
import com.tanyde.mapper.PermissionMapper;
import com.tanyde.result.PageResult;
import com.tanyde.service.PermissionService;
import com.tanyde.service.RedisService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanyde.mapper.RoleMapper;
import com.tanyde.vo.PermissionGroupVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 根据ID获得权限信息
     *
     * @param id
     * @return com.tanyde.dto.LoginDTO.RoleDTO
     * @date 2026/2/22 16:31
     **/
    @Override
    public PermissionDTO getById(Long id) {
        PermissionDTO permissionDTO = new PermissionDTO();
        //获取role数据
        Permission permission = permissionMapper.selectById(id);
        BeanUtils.copyProperties(permission, permissionDTO);
        return permissionDTO;
    }

    /**
     * 根据EmployeeId获得权限codes
     *
     * @param employeeId
     * @return java.utils.List<java.lang.String>
     * @date 2026/2/22 16:31
     **/
    @Override
    public List<String> getCodesByIds(Long employeeId) {
        // 1. 获取用户角色
        //先走缓存
        String cacheKey = RedisConstant.EMPLOYEE_PERMISSION_PREFIX + employeeId;
        Object cacheValue = redisService.get(cacheKey);
        if (cacheValue instanceof List) {
            return (List<String>) cacheValue;
        }
        //若未命中则查数据库
        String roleCode = roleMapper.getRoleCodeByEmployeeId(employeeId);
        // 2. 如果是超级管理员，返回所有权限
        List<String> codes;
        if ("super_admin".equals(roleCode)) {
            codes = permissionMapper.listAll();
        }
        // 3. 否则查询该用户拥有的权限
        else {
            codes = permissionMapper.getCodesByEmployeeId(employeeId);
        }
        //缓存到redis
        redisService.set(cacheKey, codes, RedisConstant.EMPLOYEE_PERMISSION_TTL, TimeUnit.SECONDS);
        return codes;
    }

    /**
     * 分页查询权限信息
     *
     * @param permissionPQDTO
     * @return com.tanyde.result.PageResult
     * @date 2026/2/22 16:32
     **/
    @Override
    public PageResult pageQuery(PermissionPageQueryDTO permissionPQDTO) {
        //如果为null，设定初始值
        if (permissionPQDTO == null) {
            permissionPQDTO = new PermissionPageQueryDTO();
        }
        Integer pageIndex = permissionPQDTO.getPage() == null ? 1 : permissionPQDTO.getPage();
        Integer pageSize = permissionPQDTO.getPageSize() == null ? 10 : permissionPQDTO.getPageSize();
        //构建key
        String CacheKey = RedisConstant.PERMISSION_LIST_PREFIX + ":page:"
                + buildPermissionPageCacheKey(permissionPQDTO, pageIndex, pageSize);
        //从缓存获取
        Object cacheValue = redisService.get(CacheKey);
        if (cacheValue instanceof List) {
            return (PageResult) cacheValue;
        }
        //未命中从数据库查
        Page<Permission> page = new Page<>(pageIndex, pageSize);
        IPage<Permission> resultPage = permissionMapper.pageQuery(page, permissionPQDTO);

        long total = resultPage.getTotal();
        //将PO转化为DTO的list
        List<PermissionDTO> records = resultPage.getRecords().stream()
                .map(permission -> {
                    PermissionDTO permissionDTO = new PermissionDTO();
                    BeanUtils.copyProperties(permission, permissionDTO);
                    return permissionDTO;
                }).collect(Collectors.toList());
        PageResult result= new PageResult(total, records);
        //存入缓存
        redisService.set(CacheKey, result, RedisConstant.PERMISSION_LIST_TTL, TimeUnit.SECONDS);

        return result;
    }

    /**
     * 获得所有权限code
     *
     * @return java.utils.List<com.tanyde.dto.LoginDTO.RoleDTO>
     * @date 2026/2/22 16:32
     **/
    @Override
    public List<String> listAll() {
        //从缓存获取
        Object cacheValue = redisService.get(RedisConstant.PERMISSION_LIST_PREFIX);
        if (cacheValue instanceof List){
            return (List<String>) cacheValue;
        }
        //未命中则查数据库
        List<String> result=permissionMapper.listAll();
        //存入缓存
        redisService.set(RedisConstant.PERMISSION_LIST_PREFIX, result,
                RedisConstant.PERMISSION_LIST_TTL, TimeUnit.SECONDS);
        return result;
    }

    /**
     * 获取权限树
     *
     * @return java.utils.List<com.tanyde.vo.PermissionGroupVO>
     * @date 2026/2/24
     **/
    @Override
    public List<PermissionGroupVO> getTree() {
        // 1. 获取所有权限
        //构建key
        Object cacheValue = redisService.get(RedisConstant.PERMISSION_TREE_PREFIX);
        //从缓存获取
        if (cacheValue instanceof List) {
            return (List<PermissionGroupVO>) cacheValue;
        }
        //未命中则查数据库
        List<Permission> allPermissions = permissionMapper.findAll();
        // 2. 按模块分组 (这里简单通过code的前缀来分组，例如 activity:add -> activity)
        Map<String, List<Permission>> groupedPermissions = allPermissions.stream()
                .collect(Collectors.groupingBy(p -> {
                    String code = p.getCode();
                    if (code != null && code.contains(":")) {
                        return code.split(":")[0];
                    }
                    return "other";
                }));

        // 3. 构建返回结果
        List<PermissionGroupVO> result = new ArrayList<>();
        // 定义模块名称映射（可选）
        Map<String, String> moduleNames = Map.of(
                "activity", "活动管理",
                "employee", "员工管理",
                "system", "系统管理",
                "role", "角色管理"
        );

        groupedPermissions.forEach((key, list) -> {
            String moduleName = moduleNames.getOrDefault(key, key);
            // 如果key是system:role这种形式的，可能需要特殊处理，这里暂且简化
            if ("other".equals(key)) moduleName = "其他";

            result.add(PermissionGroupVO.builder()
                    .module(moduleName)
                    .permissions(list)
                    .build());
        });
        //存入缓存
        redisService.set(RedisConstant.PERMISSION_TREE_PREFIX, result,
                RedisConstant.PERMISSION_TREE_TTL, TimeUnit.SECONDS);
        return result;
    }

    /**
     * 封装分页缓存key
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return java.lang.String
     * @date 2026/3/13 16:21
     **/
    private String buildPermissionPageCacheKey(PermissionPageQueryDTO dto, int page, int pageSize) {
        StringBuilder builder = new StringBuilder();
        builder.append(formatKeyPart(dto.getName())).append("|")
                .append(formatKeyPart(dto.getCode())).append("|")
                .append(formatKeyPart(dto.getDescription())).append("|")
                .append(page).append("|")
                .append(pageSize);
        return builder.toString();
    }
    //解决null
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
