package com.tanyde.handler;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.tanyde.constant.MessageConstant;
import com.tanyde.exception.BaseException;
import com.tanyde.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * 全局异常处理器
 *
 * @return
 * @author TanyDe
 * @create 2026/1/3
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常（你自己抛的）
     */
    @ExceptionHandler(BaseException.class)
    public Result<String> handleBaseException(BaseException ex) {
        log.warn("业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * SQL 约束异常（唯一键、非空等）
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> handleSqlException(SQLIntegrityConstraintViolationException ex) {
        log.error("数据库约束异常", ex);

        String message = ex.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            // MySQL 唯一键异常示例：
            // Duplicate entry 'admin' for key 'employee.username'
            String value = message.split("'")[1];
            return Result.error(value + MessageConstant.ALREADY_EXISTS);
        }

        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    /**
     * Spring 数据完整性异常（非常重要！）
     * NOT NULL / 外键 / 长度超限 都在这里
     */
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public Result<String> handleDataIntegrityViolationException(Exception ex) {
        log.error("数据完整性异常", ex);
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    /**
     * 兜底异常（必须有）
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    /**
     * 捕获 Sa-Token 未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public Result<String> handleNotLoginException(NotLoginException nle) {
        String message = "";
        if(nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供 Token";
        } else if(nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "Token 无效";
        } else if(nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "Token 已过期";
        } else if(nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "Token 已被顶下线";
        } else if(nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "Token 已被踢下线";
        } else {
            message = "当前会话未登录";
        }
        // 这里返回 401 状态码或自定义的未登录状态码
        return Result.error(message);
    }

    /**
     * 捕获 Sa-Token 无权限异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result<String> handleNotPermissionException(NotPermissionException e) {
        return Result.error("无此权限：" + e.getCode());
    }

    /**
     * 捕获 Sa-Token 无角色异常
     */
    @ExceptionHandler(NotRoleException.class)
    public Result<String> handleNotRoleException(NotRoleException e) {
        return Result.error("无此角色：" + e.getRole());
    }

}

