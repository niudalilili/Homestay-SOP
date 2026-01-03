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
     * 捕获业务异常
     *
     * @param ex
     * @return com.tanyde.result.Result
     * @author TanyDe
     * @create 2026/1/3
     **/
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息:{}",ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message=ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split=message.split("");
            String username=split[2];
            String msg=username+ MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
