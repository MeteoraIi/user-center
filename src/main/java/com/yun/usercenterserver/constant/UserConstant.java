package com.yun.usercenterserver.constant;

/**
 * 用户常量，表一些状态、权限...
 */
public interface UserConstant {

    /**
     *  用户登录态键
     */
    // 看作一个 key
    String USER_LOGIN_STATE = "userLoginState";

    // ----- 权限 ------

    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;
}
