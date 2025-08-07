package com.yun.usercenterserver.exception;

import com.yun.usercenterserver.common.ErrorCode;

// RuntimeException 不需要用 throw 显示的捕获这个异常
// 这个异常内部就没有我的 ERRORCODE
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    // 从 ErrorCode 中记录的状态信息， 来构造异常
    public BusinessException(ErrorCode errorCode) {
        this(errorCode.getMessage(), errorCode.getCode(), errorCode.getDescription());
    }

    public BusinessException(ErrorCode errorCode, String description) {
        this(errorCode.getMessage(), errorCode.getCode(), description);
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
