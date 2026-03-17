package com.tanyde.config;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MpMetaObjectHandler implements MetaObjectHandler {
    //插入填充
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        Long currentId = getCurrentUserId();

        strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        strictInsertFill(metaObject, "createUser", Long.class, currentId);
        strictInsertFill(metaObject, "updateUser", Long.class, currentId);
    }
    //更新填充
    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        Long currentId = getCurrentUserId();

        strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
        strictUpdateFill(metaObject, "updateUser", Long.class, currentId);
    }
    //获取当前用户ID
    private Long getCurrentUserId() {
        try {
            return StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            return 0L;
        }
    }
}