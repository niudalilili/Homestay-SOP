package com.tanyde.enumeration;

import lombok.Getter;

/**
 * 活动方案状态枚举
 */
@Getter
public enum ActivityStatus {
    DRAFT(1, "草稿"),
    ONLINE(2, "上线"),
    OFFLINE(3, "下线");

    private final Integer code;
    private final String msg;

    ActivityStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}