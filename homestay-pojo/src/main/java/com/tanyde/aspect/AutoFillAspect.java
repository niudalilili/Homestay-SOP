package com.tanyde.aspect;


import cn.dev33.satoken.stp.StpUtil;
import com.tanyde.constant.AutoFillConstant;
import com.tanyde.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.tanyde.annotation.AutoFill;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     *
     * @return
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Pointcut("execution(* com.tanyde.mapper.*.*(..)) && @annotation(com.tanyde.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     *
     * @param joinPoint
     * @return void
     * @author TanyDe
     * @create 2026/1/4
     **/
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自定填充");
        //获取到当前被拦截的方法上的数据库操纵类型
        //方法签名对象
        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        //获取方法上的注解对象
        AutoFill autoFill=signature.getMethod().getAnnotation(AutoFill.class);
        //获得数据库操纵类型
        OperationType operationType=autoFill.value();

        //获取到当前被拦截的方法的参数 -- 实体对象
        Object[]args=joinPoint.getArgs();
        if(args==null || args.length==0){
            return ;
        }
        Object entity=args[0];
        //准备赋值的数据
        LocalDateTime now=LocalDateTime.now();
        Long currentId;
        try {
            currentId = StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            currentId = 0L; // 系统用户或未登录用户
        }

        //根据不同类型，对应属性进行反射来赋值
        if(operationType==OperationType.INSERT){
            //为四个公共字段赋值
            try{
                //通过反射获得setter方法
                Method setCreateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setCreateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                //通过反射为对象赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        } else if (operationType==OperationType.UPDATE) {
            //为两个公共字段赋值
            try{
                //通过反射获得getter方法
                Method setUpdateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                //通过反射为对象赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
