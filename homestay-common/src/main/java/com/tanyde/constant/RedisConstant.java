package com.tanyde.constant;

// Redis 常量
public class RedisConstant {

     // 微信登录凭证缓存前缀
    public static final String WX_LOGIN_PREFIX = "wx:login:";

     // 用户信息缓存前缀
    public static final String USER_INFO_PREFIX = "user:info:";


    // 活动详情缓存前缀
    public static final String ACTIVITY_DETAIL_PREFIX = "activity:detail:";

     // 活动列表缓存前缀
    public static final String ACTIVITY_LIST_PREFIX = "activity:list:";

     // 收藏列表缓存前缀
    public static final String FAVORITE_LIST_PREFIX = "favorite:list:";

     // 反馈列表缓存前缀
    public static final String FEEDBACK_LIST_PREFIX = "feedback:list:";

     // 员工权限信息缓存前缀
    public static final String EMPLOYEE_PERMISSION_PREFIX = "employee:permission:";

    public static final String EMPLOYEE_ROLE_PREFIX = "employee:role:";

    public static final String ROLE_PAGE_PREFIX = "role:page:";

    public static final String ROLE_DETAIL_PREFIX = "role:detail:";

    public static final String PERMISSION_TREE_PREFIX = "permission:tree";

    public static final String PERMISSION_LIST_PREFIX = "permission:list";

     // 验证码缓存前缀
    public static final String VERIFICATION_CODE_PREFIX = "code:";

    // ==================== 缓存过期时间（秒） ====================

    /**
     * 微信登录凭证：7 天
     */
    public static final long WX_LOGIN_TTL = 7 * 24 * 60 * 60;

    /**
     * 用户信息：2 小时
     */
    public static final long USER_INFO_TTL = 2 * 60 * 60;

    /**
     * 活动详情：1 小时
     */
    public static final long ACTIVITY_DETAIL_TTL = 60 * 60;

    /**
     * 活动列表：30 分钟
     */
    public static final long ACTIVITY_LIST_TTL = 30 * 60;

    /**
     * 收藏列表：30 分钟
     */
    public static final long FAVORITE_LIST_TTL = 30 * 60;

    /**
     * 员工权限：2 小时
     */
    public static final long EMPLOYEE_PERMISSION_TTL = 2 * 60 * 60;

    public static final long EMPLOYEE_ROLE_TTL = 2 * 60 * 60;

    public static final long ROLE_PAGE_TTL = 30 * 60;

    public static final long ROLE_DETAIL_TTL = 30 * 60;

    public static final long PERMISSION_TREE_TTL = 30 * 60;

    public static final long PERMISSION_LIST_TTL = 30 * 60;

    /**
     * 验证码：5 分钟
     */
    public static final long VERIFICATION_CODE_TTL = 5 * 60;
}
