package com.tanyde.handler;


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
}

