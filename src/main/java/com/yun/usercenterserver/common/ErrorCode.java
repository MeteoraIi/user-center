package com.yun.usercenterserver.common;

import lombok.Data;

/**
 * 错误码
 */
// @Data 无法用

// ErrorCode 单纯就是记录这些信息，生成异常的时候直接从中获取
public enum ErrorCode {

    /**
     * 枚举常量
     */
    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    PARAMS_NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN_ERROR(40100, "未登录", ""),
    NO_AUTH_ERROR(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统异常", ""),
    DATA_ERROR(60000, "数据错误", ""),
    NO_MATCH_ERROR(60001, "账号密码不匹配", ""),
    ;

    /**
     * 枚举类型的成员变量
     */
    private final int code;
    // 错误码信息
    private final String message;
    // 对错误码的详细描述
    private final String description;

    // 枚举的构造函数默认是 private
    ErrorCode(int code, String message, String description) {
        this.message = message;
        this.description = description;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
