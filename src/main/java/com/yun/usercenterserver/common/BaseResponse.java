package com.yun.usercenterserver.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * 后端返给前端的数据用此类封装
 * 使用泛型可以提供该类通用性
 * 泛型的参数不能是基本类型
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {

    // 0 - 成功 1 - 失败
    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse() {
    }

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    // 这个怎么调用，传两个参数。。
    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, String message, String description) {
        this(code, null, message, description);
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public BaseResponse(ErrorCode errorCode, String message, String description) {
        this(errorCode.getCode(), null, message, description);
    }


}